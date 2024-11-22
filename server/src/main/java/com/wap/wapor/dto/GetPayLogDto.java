package com.wap.wapor.dto;

import com.wap.wapor.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPayLogDto {
    private String title;
    private String content;
    private String imgUrl;
    private String category;
    private Long amount;
    private int likeCount;
    private String user_id;
    private String user_nickname;

}

