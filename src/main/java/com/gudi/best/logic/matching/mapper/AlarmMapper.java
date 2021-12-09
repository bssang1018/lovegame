package com.gudi.best.logic.matching.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper {

	@Insert("INSERT INTO alarm(sender, reciver, message) VALUES(#{param1},#{param2},#{param3})")
	void alarmInsert(String loginId, String reciver, String message);

	
}
