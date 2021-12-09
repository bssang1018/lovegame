package com.gudi.best.logic.matching.dto;

import lombok.Data;

@Data
public class ChatDTO {
	
	private Integer roomNum;
	private String id;
	private String message;
	private String dates;
	private String imgPath;
	private Integer maxRowNum;
	private String receiver;
}
