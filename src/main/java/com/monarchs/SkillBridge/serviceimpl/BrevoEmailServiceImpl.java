package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.service.BrevoEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BrevoEmailServiceImpl implements BrevoEmailService {

    private final String apikey;

    private final String senderEmail;

    private final String senderName;

    private final RestClient restClient;

    private static final String BREVO_API_URL="https://api.brevo.com/v3/smtp/email";


    public BrevoEmailServiceImpl(@Value("${BREVO_SENDER_EMAIL}") String senderEmail,
                                 @Value("${BREVO_API_KEY}") String apikey,
                                 @Value("${BREVO_SENDER_NAME}") String senderName,
                                 @Qualifier("brevoRestClient") RestClient restClient) {
        this.senderEmail = senderEmail;
        this.apikey = apikey;
        this.senderName = senderName;
        this.restClient = restClient;

    }

    @Override
    public void sendEmail(String receiver, String subject, String message) {
        Map<String,Object> sender=new HashMap<>();
        sender.put("email",senderEmail);
        sender.put("name",senderName);

        Map<String,Object> recipient =new HashMap<>();
        recipient.put("email",receiver);

        Map<String,Object> requestBody=new HashMap<>();
        requestBody.put("sender",sender);
        requestBody.put("to", List.of(recipient));
        requestBody.put("subject",subject);
        requestBody.put("htmlContent",message);

        try {
            restClient.post()
                    .uri(BREVO_API_URL)
                    .header("api-key",apikey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Email sent successfully to {}",receiver);
        } catch (Exception e) {
            log.error("Failed to sent email to {}: {}",receiver,e.getMessage());
            throw new RuntimeException("Failed to sent email via brevo",e);
        }
    }
}
