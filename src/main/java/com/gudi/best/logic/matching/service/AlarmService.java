package com.gudi.best.logic.matching.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gudi.best.logic.matching.mapper.AlarmMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AlarmService {

	private final SimpMessagingTemplate template;
	private final AlarmMapper alarmMapper;

	@Transactional
	public void alarmMessage(String loginId, String reciver, String message) {
		HashMap<String, String> alarmMap = new HashMap<String, String>();
		alarmMap.put("person", reciver);
		alarmMap.put("alarmText", message);
		template.convertAndSend("/sub/alarm/chatAlarm", alarmMap);
		alarmMapper.alarmInsert(loginId, reciver, message);

	}

}
