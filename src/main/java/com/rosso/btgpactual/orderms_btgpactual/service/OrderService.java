package com.rosso.btgpactual.orderms_btgpactual.service;

import com.rosso.btgpactual.orderms_btgpactual.controller.dto.OrderResponse;
import com.rosso.btgpactual.orderms_btgpactual.entity.OrderEntity;
import com.rosso.btgpactual.orderms_btgpactual.entity.OrderItem;
import com.rosso.btgpactual.orderms_btgpactual.listener.dto.OrderCreatedEvent;
import com.rosso.btgpactual.orderms_btgpactual.listener.dto.OrderItemEvent;
import com.rosso.btgpactual.orderms_btgpactual.mapper.OrderMapper;
import com.rosso.btgpactual.orderms_btgpactual.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@AllArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final MongoTemplate mongoTemplate;
    private final OrderMapper orderMapper;

    public void save(OrderCreatedEvent event) {

        var entity = new OrderEntity();

        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setItems(getOrderItems(event.itens()));
        entity.setTotal(getTotal(event.itens()));

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(orderMapper::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId){
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, OrderEntity.class, Document.class);

        return Optional.ofNullable(response.getUniqueMappedResult())
                .map(r -> r.get("total"))
                .map(val -> new BigDecimal(val.toString()))
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getTotal(List<OrderItemEvent> items) {
        return items.stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(List<OrderItemEvent> items) {
        return items.stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
