package com.wap.wapor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostPayLogDto {
    private String title;
    private String content;
    private String imgUrl;
    private String category;
    private String amount;

}

