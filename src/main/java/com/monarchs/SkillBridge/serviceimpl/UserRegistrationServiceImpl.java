package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.dto.ResendOtpDto;
import tools.jackson.databind.ObjectMapper;
import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.dto.OtpVerifyDto;
import com.monarchs.SkillBridge.dto.TempUserRegistrationDto;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.enums.UserStatus;
import com.monarchs.SkillBridge.event.EmailEvent;
import com.monarchs.SkillBridge.repository.UserRepository;
import com.monarchs.SkillBridge.service.UserRegistrationService;
import com.monarchs.SkillBridge.strategy.RoleRegistrationDispatcher;
import com.monarchs.SkillBridge.utils.EmailBuilder;
import com.monarchs.SkillBridge.utils.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.monarchs.SkillBridge.config.RabbitMqConfig.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepo;

    private final RedisTemplate<String,String> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    private final RoleRegistrationDispatcher dispatcher;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;


    @Override
    public String initiateUserRegistrationService(BaseUserDto dto, UserRole role) {
        if(userRepo.existsByEmail(dto.getEmail())){
            throw new RuntimeException("User already exists, Go to login");
        }

        String otp= OtpGenerator.generateOtp(6);

//      *** String otp and email in Redis for verification
        TempUserRegistrationDto tempData=TempUserRegistrationDto.builder()
                .tempOtp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(3))
                .role(role)
                .baseUserDto(dto)
                .build();
        try {
            String jsonData=objectMapper.writeValueAsString(tempData);
            redisTemplate.opsForValue().set(dto.getEmail(), jsonData, Duration.ofMinutes(3));
            log.debug("Temp data stored in redis with key: {}",dto.getEmail());
            log.info(jsonData);
        } catch (Exception e) {
            log.error("Failed to store data in redis");
            throw new RuntimeException(e.getMessage()+"Failed to store data in redis");
        }

        String emailBody= EmailBuilder.otpEmailTemplateBuilder(dto.getUsername(),otp);
        EmailEvent emailEvent=EmailEvent.builder()
                .receiverEmail(dto.getEmail())
                .subject("Verify your email")
                .message(emailBody)
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_OTP_ROUTING_KEY,emailEvent);

        return "Otp sent Successfully to "+dto.getEmail()+" and it it valid for 5 minutes";
    }

    @Transactional
    @Override
    public String finalUserRegistrationService(OtpVerifyDto dto) {
        TempUserRegistrationDto tempUserData;
        try {
            String jsonData=redisTemplate.opsForValue().get(dto.getEmail());
            if(jsonData==null) {
                throw new RuntimeException("Data in redis is corrupted or deleted");
            }
            tempUserData=objectMapper.readValue(jsonData,TempUserRegistrationDto.class);
            log.info("Success");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage()+"failed to get data");
        }

        if (!tempUserData.getTempOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Incorrect OTP");
        }

        if(LocalDateTime.now().isAfter(tempUserData.getExpiryTime())){
            redisTemplate.delete(dto.getEmail());
            throw new RuntimeException("Time limit exceeded");
        }

        BaseUserDto baseUserDto=tempUserData.getBaseUserDto();
        UserRole role=tempUserData.getRole();

        User user=User.builder()
                .username(baseUserDto.getUsername())
                .email(baseUserDto.getEmail())
                .password(passwordEncoder.encode(baseUserDto.getPassword()))
                .phone(baseUserDto.getPhone())
                .role(role)
                .status(role==UserRole.STUDENT? UserStatus.ACTIVE:UserStatus.PENDING_APPROVAL)
                .build();
        User savedUser=userRepo.save(user);

        dispatcher.completeRegistration(role,baseUserDto,savedUser);

        if(savedUser.getStatus().equals(UserStatus.ACTIVE)){
            String emailBody=EmailBuilder.welcomeEmailBuilder(tempUserData.getBaseUserDto().getUsername());
            EmailEvent emailEvent=EmailEvent.builder()
                    .receiverEmail(tempUserData.getBaseUserDto().getEmail())
                    .subject("Welcome to NexCart!")
                    .message(emailBody)
                    .build();

            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_WELCOME_ROUTING_KEY,emailEvent);
        }
        redisTemplate.delete(dto.getEmail());
        return "User registration completed!";
    }

    @Override
    public String resendOtpService(ResendOtpDto dto) {
        String email=dto.getEmail();

        String jsonData=redisTemplate.opsForValue().get(email);

        if(jsonData==null){
            throw new RuntimeException("No pending registration found or OTP has expired");
        }

        TempUserRegistrationDto tempUserData;

        try {
            tempUserData=objectMapper.readValue(jsonData, TempUserRegistrationDto.class);
        }catch (Exception e){
            log.error("Failed to read temporary registration data for {}", email, e);
            throw new RuntimeException("Failed to process OTP request");
        }

        String newOtp=OtpGenerator.generateOtp(6);

        tempUserData.setTempOtp(newOtp);
        tempUserData.setExpiryTime(LocalDateTime.now().plusMinutes(3));

        try {
            String updatedJsonData=objectMapper.writeValueAsString(tempUserData);
            redisTemplate.opsForValue().set(email,updatedJsonData,Duration.ofMinutes(3));
        }catch (Exception e){
            log.error("Failed to update OTP in Redis for {}", email, e);
            throw new RuntimeException("Failed to store new OTP");
        }

        String emailBody= EmailBuilder.otpEmailTemplateBuilder(tempUserData.getBaseUserDto().getUsername(),newOtp);
        EmailEvent emailEvent=EmailEvent.builder()
                .receiverEmail(email)
                .subject("Your new verification OTP")
                .message(emailBody)
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_OTP_ROUTING_KEY,emailEvent);

        return "A new otp has been sent to "+email;
    }

}
