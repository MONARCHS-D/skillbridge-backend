package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserStatus;
import com.monarchs.SkillBridge.event.EmailEvent;
import com.monarchs.SkillBridge.repository.UserRepository;
import com.monarchs.SkillBridge.service.AdminService;
import com.monarchs.SkillBridge.utils.EmailBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.monarchs.SkillBridge.config.RabbitMqConfig.EMAIL_EXCHANGE;
import static com.monarchs.SkillBridge.config.RabbitMqConfig.EMAIL_WELCOME_ROUTING_KEY;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepo;

    private final RabbitTemplate rabbitTemplate;

    public AdminServiceImpl(UserRepository userRepo, RabbitTemplate rabbitTemplate) {
        this.userRepo = userRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @Override
    public String approveUserService(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        if(user.getStatus()!= UserStatus.PENDING_APPROVAL){
            throw new RuntimeException("User already active");
        }
        user.setStatus(UserStatus.ACTIVE);
        userRepo.save(user);

        String emailBody= EmailBuilder.welcomeEmailBuilder(user.getUsername());
        EmailEvent emailEvent= EmailEvent.builder()
                .receiverEmail(user.getEmail())
                .subject("Welcome to NexCart!")
                .message(emailBody)
                .build();

        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_WELCOME_ROUTING_KEY,emailEvent);
        return "User approved successfully";
    }

    @Transactional
    @Override
    public String rejectUserService(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        if(user.getStatus()!= UserStatus.PENDING_APPROVAL){
            throw new RuntimeException("User already active");
        }
        user.setStatus(UserStatus.REJECTED);
        userRepo.save(user);
        return "User rejected successfully";
    }
}
