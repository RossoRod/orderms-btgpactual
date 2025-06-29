package com.rosso.btgpactual.orderms_btgpactual.controller.dto;

import java.util.List;

public record ApiResponse<T>(List<T> data,
                             PaginationResponse pagination) {
}
