package dev.expensewise.backend.config.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author arpan
 * @since 8/13/25
 */
@Configuration
public class RabbitMQConfig {
    @Value("${app.queue.otp}")
    private String otpQueue;

    @Value("${app.queue.forgot-password}")
    private String forgotPasswordQueue;

    @Value("${app.queue.reset-password}")
    private String resetPasswordQueue;

    @Value("${app.queue.change-password}")
    private String changePasswordQueue;

    @Value("${app.queue.account-created}")
    private String accountCreatedQueue;

    @Value("${app.queue.profile-event}")
    private String profileEventQueue;

    @Bean
    public Queue otpQueue() {
        return new Queue(otpQueue, true, false, false);
    }

    @Bean
    public Queue forgotPasswordQueue() {
        return new Queue(forgotPasswordQueue, true, false, false);
    }

    @Bean
    public Queue resetPasswordQueue() {
        return new Queue(resetPasswordQueue, true, false, false);
    }

    @Bean
    public Queue changePasswordQueue() {
        return new Queue(changePasswordQueue, true, false, false);
    }

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(accountCreatedQueue, true, false, false);
    }

    @Bean
    public Queue profileEventQueue() {
        return new Queue(profileEventQueue, true, false, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}

