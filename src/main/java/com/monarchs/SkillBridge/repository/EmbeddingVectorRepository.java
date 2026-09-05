package com.monarchs.SkillBridge.repository;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.StringJoiner;

@Repository
@Slf4j
public class EmbeddingVectorRepository {

    private static final int EMBEDDING_DIMENSION = 384;

    private static final int MAX_LIMIT = 100;

    private final JdbcTemplate jdbcTemplate;

    public EmbeddingVectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public record EmbeddingMatch(long id, double distance) {
    }

    public int saveStudentEmbedding(long studentId, float[] embedding) {

        validateEmbedding(embedding);

        String sql = """
                 UPDATE students SET embedding_vector = ? WHERE id = ?
                """;

        return updateEmbedding(
                sql,
                studentId,
                embedding
        );
    }

    public int savePostingEmbedding(
            long postingId,
            float[] embedding
    ) {

        validateEmbedding(embedding);

        String sql = """
                UPDATE postings
                SET embedding_vector = ?
                WHERE id = ?
                """;

        return updateEmbedding(
                sql,
                postingId,
                embedding
        );
    }


    private int updateEmbedding(String sql, long id, float[] embedding) {

        String vectorString = toPgVector(embedding);

        long startTime = System.currentTimeMillis();

        int updatedRows = jdbcTemplate.update(
                connection -> {

                    PreparedStatement ps =
                            connection.prepareStatement(sql);

                    PGobject vector =
                            createPgVector(vectorString);

                    ps.setObject(1, vector);
                    ps.setLong(2, id);

                    return ps;
                }
        );

        long elapsedTime =
                System.currentTimeMillis() - startTime;

        if (elapsedTime > 200) {
            log.debug(
                    "Embedding update took {} ms for id {}",
                    elapsedTime,
                    id
            );
        }

        return updatedRows;
    }


    public List<Long> findNearestPostingIds(float[] studentEmbedding, int limit) {

        validateEmbedding(studentEmbedding);
        validateLimit(limit);

        String sql = """
                SELECT id
                FROM postings
                WHERE embedding_vector IS NOT NULL
                ORDER BY embedding_vector <=> ?::vector
                LIMIT ?
                """;

        return findNearestIds(
                sql,
                studentEmbedding,
                limit
        );
    }

    //cosine
    public List<EmbeddingMatch> findNearestPostingsWithDistance(float[] studentEmbedding, int limit) {

        validateEmbedding(studentEmbedding);
        validateLimit(limit);

        String sql = """
                SELECT
                    id,
                    embedding_vector <=> ?::vector AS distance
                FROM postings
                WHERE embedding_vector IS NOT NULL
                ORDER BY distance
                LIMIT ?
                """;

        return queryForMatches(
                sql,
                studentEmbedding,
                limit
        );
    }


    public List<Long> findNearestStudentIds(float[] postingEmbedding, int limit) {

        validateEmbedding(postingEmbedding);
        validateLimit(limit);

        String sql = """
                SELECT id
                FROM students
                WHERE embedding_vector IS NOT NULL
                ORDER BY embedding_vector <=> ?::vector
                LIMIT ?
                """;

        return findNearestIds(
                sql,
                postingEmbedding,
                limit
        );
    }

    //cosine
    public List<EmbeddingMatch> findNearestStudentsWithDistance(float[] postingEmbedding, int limit) {

        validateEmbedding(postingEmbedding);
        validateLimit(limit);

        String sql = """
                SELECT
                    id,
                    embedding_vector <=> ?::vector AS distance
                FROM students
                WHERE embedding_vector IS NOT NULL
                ORDER BY distance
                LIMIT ?
                """;

        return queryForMatches(
                sql,
                postingEmbedding,
                limit
        );
    }


    private List<Long> findNearestIds(String sql, float[] embedding, int limit) {

        String vectorString = toPgVector(embedding);

        return jdbcTemplate.query(
                connection -> {

                    PreparedStatement ps =
                            connection.prepareStatement(sql);

                    PGobject vector =
                            createPgVector(vectorString);

                    ps.setObject(1, vector);
                    ps.setInt(2, limit);

                    return ps;
                },
                (rs, rowNum) -> rs.getLong("id")
        );
    }


    private List<EmbeddingMatch> queryForMatches(String sql, float[] embedding, int limit) {

        String vectorString = toPgVector(embedding);

        return jdbcTemplate.query(
                connection -> {

                    PreparedStatement ps =
                            connection.prepareStatement(sql);

                    PGobject vector =
                            createPgVector(vectorString);

                    ps.setObject(1, vector);
                    ps.setInt(2, limit);

                    return ps;
                },
                (rs, rowNum) ->
                        new EmbeddingMatch(
                                rs.getLong("id"),
                                rs.getDouble("distance")
                        )
        );
    }


    //Convert float[] to PostgreSQL pgvector format.
    private String toPgVector(float[] embedding) {

        validateEmbedding(embedding);

        StringJoiner joiner =
                new StringJoiner(",", "[", "]");

        for (float value : embedding) {

            if (Float.isNaN(value) ||
                    Float.isInfinite(value)) {

                throw new IllegalArgumentException(
                        "Embedding contains invalid value: " + value
                );
            }

            joiner.add(Float.toString(value));
        }

        return joiner.toString();
    }


    private PGobject createPgVector(String vectorString) {

        try {
            PGobject vector = new PGobject();

            vector.setType("vector");
            vector.setValue(vectorString);

            return vector;

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to create PostgreSQL vector",
                    e
            );
        }
    }


    private void validateEmbedding(float[] embedding) {

        if (embedding == null) {

            throw new IllegalArgumentException(
                    "Embedding cannot be null"
            );
        }

        if (embedding.length != EMBEDDING_DIMENSION) {

            throw new IllegalArgumentException(
                    "Expected embedding dimension "
                            + EMBEDDING_DIMENSION
                            + " but got "
                            + embedding.length
            );
        }

        for (float value : embedding) {

            if (Float.isNaN(value) ||
                    Float.isInfinite(value)) {

                throw new IllegalArgumentException(
                        "Embedding contains NaN or Infinity"
                );
            }
        }
    }

    private void validateLimit(int limit) {

        if (limit <= 0 || limit > MAX_LIMIT) {

            throw new IllegalArgumentException(
                    "Limit must be between 1 and "
                            + MAX_LIMIT
            );
        }
    }
}