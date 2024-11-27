package com.wap.wapor.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostPayLogDto {
    private String title;
    private String content;
    private String imgUrl;
    private String category;
    private Long amount;
    private int  isPublic;

}

