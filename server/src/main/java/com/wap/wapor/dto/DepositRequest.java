package com.wap.wapor.dto;

import lombok.Data;

@Data
public class DepositRequest {
    private Long amount;
    private Long accountId;
    private String identifier;
}
