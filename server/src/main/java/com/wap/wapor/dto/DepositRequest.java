package com.wap.wapor.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DepositRequest {
    @Min(value = 1, message = "입금 금액은 0보다 커야 합니다.")
    private Long amount;
    private Long accountId;
    private String identifier;
}
