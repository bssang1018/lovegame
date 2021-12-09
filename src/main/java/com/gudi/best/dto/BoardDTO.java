package com.gudi.best.dto;

import lombok.Data;

@Data
public class BoardDTO {
    private int boardNum;
    private String id;
    private String content;
    private String title;
    private String date;
    private int boardHit;
    private int goodCount;
    private String path;
    private String imgPath;
}
