package com.monarchs.SkillBridge.eventlisteners;

import com.monarchs.SkillBridge.config.RabbitMqConfig;
import com.monarchs.SkillBridge.event.EmailEvent;
import com.monarchs.SkillBridge.service.BrevoEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailEventListener {

    private final BrevoEmailService brevoEmailService;

    @RabbitListener(queues = RabbitMqConfig.EMAIL_OTP_QUEUE)
    public void sendOtpEmail(EmailEvent emailEvent){
        try {
            brevoEmailService.sendEmail(emailEvent.getReceiverEmail(), emailEvent.getSubject(), emailEvent.getMessage());
            log.info("Received OTP email event for {}", emailEvent.getReceiverEmail());
        } catch (Exception e) {
            log.error("Couldn't receive otp email event for{}",emailEvent.getReceiverEmail());
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMqConfig.EMAIL_WELCOME_QUEUE)
    public void sendWelcomeEmail(EmailEvent emailEvent){
        try {
            brevoEmailService.sendEmail(emailEvent.getReceiverEmail(), emailEvent.getSubject(), emailEvent.getMessage());
            log.info("Received welcome email event for {}", emailEvent.getReceiverEmail());
        } catch (Exception e) {
            log.error("Couldn't receive welcome email event for{}",emailEvent.getReceiverEmail());
            throw new RuntimeException(e);
        }
    }
}
