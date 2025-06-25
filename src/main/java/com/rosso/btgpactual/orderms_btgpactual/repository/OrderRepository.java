package com.rosso.btgpactual.orderms_btgpactual.repository;

import com.rosso.btgpactual.orderms_btgpactual.entity.OrderEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

@Scope
public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
}
