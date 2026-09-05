package com.monarchs.SkillBridge.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //  Exchange
    public static final String EMAIL_EXCHANGE="email.exchange";

    //  Queues
    public static final String EMAIL_OTP_QUEUE="email.otp.queue";
    public static final String EMAIL_WELCOME_QUEUE="email.welcome.queue";


    //  Routing Keys
    public static final String EMAIL_OTP_ROUTING_KEY="email.otp.send";
    public static final String EMAIL_WELCOME_ROUTING_KEY="email.welcome.send";


    @Bean
    public Queue emailOtpQueue(){
        return QueueBuilder.durable(EMAIL_OTP_QUEUE).build();
    }

    @Bean
    public Queue emailWelcomeQueue(){return QueueBuilder.durable(EMAIL_WELCOME_QUEUE).build();}


    @Bean
    public DirectExchange emailExchange(){
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding emailOtpBinding(){
        return BindingBuilder.bind(emailOtpQueue())
                .to(emailExchange())
                .with(EMAIL_OTP_ROUTING_KEY);
    }

    @Bean
    public Binding emailWelcomeBinding(){
        return BindingBuilder.bind(emailWelcomeQueue())
                .to(emailExchange())
                .with(EMAIL_WELCOME_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}

