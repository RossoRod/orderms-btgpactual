package com.rosso.btgpactual.orderms_btgpactual.listener;

import com.rosso.btgpactual.orderms_btgpactual.listener.dto.OrderCreatedEvent;
import com.rosso.btgpactual.orderms_btgpactual.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.rosso.btgpactual.orderms_btgpactual.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
@AllArgsConstructor
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    @Autowired
    private final OrderService orderService;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message){
        logger.info("Message consumed: {}", message);

        orderService.save(message.getPayload());
    }
}
