package com.rosso.btgpactual.orderms_btgpactual.service;

import com.rosso.btgpactual.orderms_btgpactual.controller.dto.OrderResponse;
import com.rosso.btgpactual.orderms_btgpactual.entity.OrderEntity;
import com.rosso.btgpactual.orderms_btgpactual.entity.OrderItem;
import com.rosso.btgpactual.orderms_btgpactual.listener.dto.OrderCreatedEvent;
import com.rosso.btgpactual.orderms_btgpactual.listener.dto.OrderItemEvent;
import com.rosso.btgpactual.orderms_btgpactual.mapper.OrderMapper;
import com.rosso.btgpactual.orderms_btgpactual.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
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
