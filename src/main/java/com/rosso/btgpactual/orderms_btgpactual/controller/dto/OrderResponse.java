package com.rosso.btgpactual.orderms_btgpactual.controller.dto;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long customerId,
                            BigDecimal total) {
}
