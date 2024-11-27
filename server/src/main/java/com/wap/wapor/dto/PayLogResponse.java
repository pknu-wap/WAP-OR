package com.wap.wapor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayLogResponse {
    private Long payLogId;  // 생성된 PayLog의 ID
    private Long balance;   // 현재 가상계좌 잔액
}
