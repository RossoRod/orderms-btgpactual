package com.rosso.btgpactual.orderms_btgpactual.mapper;

import com.rosso.btgpactual.orderms_btgpactual.controller.dto.OrderResponse;
import com.rosso.btgpactual.orderms_btgpactual.entity.OrderEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper {
    OrderResponse fromEntity (OrderEntity entity);
}
