package com.fintransact.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = "transaction-notifications")
    public void receiveMessage(String message) {
        logger.info("Received Message: {}", message);
        // Here we would implement actual email sending logic
        // For now, we just log it to demonstrate the consumer is working
        System.out.println(" [x] Received '" + message + "'");
    }
}
