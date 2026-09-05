CREATE EXTENSION IF NOT EXISTS vector;

create table users(
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50) UNIQUE NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(30) NOT NULL ,
    phone           VARCHAR(32) not null unique ,
    status          VARCHAR(20) NOT NULL default 'PENDING_APPROVAL',
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create table institutions(
    id              BIGSERIAL primary key ,
    user_id         bigint unique not null references users(id) on delete cascade ,
    name            varchar(255) unique not null ,
    address         text not null ,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create table companies(
    id              BIGSERIAL primary key ,
    user_id         bigint unique not null references users(id) on delete cascade ,
    name            varchar(255) unique not null ,
    website         VARCHAR(512) not null unique ,
    contact_email   VARCHAR(255) not null unique ,
    contact_phone   VARCHAR(32) not null unique ,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create table academicians(
    id               BIGSERIAL primary key ,
    user_id          bigint unique not null references users(id) on delete cascade ,
    institution_id   bigint not null references institutions(id) on delete cascade ,
    name             varchar(255) not null ,
    designation      varchar(40) not null ,
    area_of_interest jsonb,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create table students(
    id               BIGSERIAL primary key ,
    user_id          bigint unique not null references users(id) on delete cascade ,
    institution_id   bigint not null references institutions(id) on delete cascade ,
    name             varchar(255) not null ,
    resume_url       varchar(2048) not null ,
    skills           jsonb,
    embedding_vector vector(384),
    certifications   jsonb,
    projects         jsonb,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create table postings(
    id                bigserial primary key ,
    company_id        bigint not null references companies(id) on delete cascade ,
    title             varchar(255) not null ,
    description       text not null ,
    location          varchar(255),
    is_remote         boolean default false,
    stipend           varchar(128),
    employment_type   varchar(20) not null ,
    embedding_vector  vector(384),
    required_skills   jsonb,
    metadata          jsonb,
    deadline          timestamp with time zone,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at        TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create table applications(
    id                BIGSERIAL primary key ,
    posting_id        bigint not null references postings(id) on delete cascade ,
    student_id        bigint not null references students(id) on delete cascade ,
    resume_url        varchar(2048) not null ,
    status            varchar(20) not null default 'APPLIED',
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at        TIMESTAMP WITH TIME ZONE DEFAULT now(),

    constraint unique_student_posting unique (posting_id,student_id)

);

create table assessments(
    id                bigserial primary key ,
    student_id        bigint not null references students(id) on delete cascade ,
    answers           jsonb,
    scores            jsonb,
    generated_profile jsonb,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create table activities(
    id                   bigserial primary key ,
    host_company_id      bigint references companies(id) on delete set null ,
    host_institution_id  bigint references institutions(id) on delete set null ,
    title                varchar(255) not null ,
    description          text,
    activity_type        varchar(30) not null ,
    start_date           timestamp with time zone,
    end_date             timestamp with time zone,
    location             varchar(255),
    registration_url     varchar(2048),
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT now(),

    constraint activities_exactly_one_host check (
             (host_company_id is not null and host_institution_id is null)
             or
             (host_institution_id is not null and host_company_id is null )
        )
);

create table activities_registration(
    id                   bigserial primary key ,
    activities_id        bigint not null references activities(id) on delete cascade ,
    user_id              bigint not null references users(id) on delete cascade ,
    status               varchar(30) default 'REGISTERED',
    registered_at        timestamp with time zone default now(),

    constraint unique_activity_user unique (activities_id,user_id)
);

create table mentorship(
    id                   bigserial primary key ,
    mentor_user_id       bigint not null references users(id) on delete cascade ,
    mentee_user_id       bigint not null references users(id) on delete cascade ,
    activities_id        bigint references activities(id) on delete set null ,
    start_date           timestamp with time zone,
    end_date             timestamp with time zone,
    status               varchar(20) not null default 'ACTIVE'
);

create table mentorship_feedback(
    id                    bigserial primary key ,
    mentorship_id         bigint not null references mentorship(id) on delete cascade ,
    reviewer_user_id      bigint references users(id) on DELETE set null ,
    feedback              text,
    rating                smallint check ( rating between 1 and 5),
    created_at            timestamp with time zone default now()
);

create table internship_progress(
    id                    bigserial primary key ,
    application_id        bigint not null references applications(id) on delete cascade ,
    mentor_user_id        bigint references users(id) on delete set null ,
    stage                 varchar(30) not null ,
    report_url            varchar(2048),
    notes                 text,
    reported_at           timestamp with time zone default now()
);

create table recommendation_logs(
    id                     bigserial primary key ,
    user_id                bigint references users(id) on delete set null ,
    input_payload          jsonb,
    recommendations        jsonb,
    explanation            jsonb,
    created_at             timestamp with time zone default now()
);

create table notifications(
    id                     bigserial primary key ,
    user_id                bigint references users(id) on delete set null ,
    type                   varchar(50) not null ,
    payload                jsonb,
    status                 varchar(50) not null default 'PENDING',
    sent_at                timestamp with time zone
);