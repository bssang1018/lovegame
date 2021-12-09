package com.gudi.best.dto;

import lombok.Data;

@Data
public class PhotoDTO {
    private int photoNum;
    private String id;
    private String division;
    private String oriFileName;
    private String newFileName;
    private String path;
    private int divisionNum;
}
