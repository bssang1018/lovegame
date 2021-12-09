package com.gudi.best.logic.loveBoard.service;

import com.gudi.best.dto.GoodDTO;
import com.gudi.best.logic.loveBoard.mapper.GoodMapper;
import com.gudi.best.logic.matching.service.AlarmService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Log4j2
@RequiredArgsConstructor
public class GoodService {

	private final GoodMapper mapper;
	private final SimpMessagingTemplate template;
	private final AlarmService alarmService;

	public boolean goodCheck(String divisionNum, String id) {
		boolean result = false;
		if (mapper.goodCheck(divisionNum, id) != null) {
			result = true;
		}
		return result;
	}

	public int goodCount(String divisionNum) {
		return mapper.goodCount(divisionNum);
	}

	public HashMap<String, Object> goodData(String boardNum, String id, String division) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		if (goodCheck(String.valueOf(boardNum), id)) {
			mapper.goodDelete(boardNum, id, division);
		} else {
			mapper.goodInsert(boardNum, id, division);
			result = true;
		}
		map.put("goodCount", goodCount(String.valueOf(boardNum)));
		map.put("check", result);
		if (division.equals("people")) {
			if(result) {
				alarmService.alarmMessage(id, boardNum, id + "님이 관심을 표현했습니다!");
			}
		}
		return map;
	}

	public ArrayList<GoodDTO> goodList(String loginId, String division) {
		return mapper.goodList(loginId, division);
	}
}
