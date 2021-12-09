package com.gudi.best.logic.admin.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.gudi.best.dto.memberDTO;
import com.gudi.best.dto.reportDTO;

@Mapper
public interface AdminMapper {

	@Select("select id,email,gender,date,admin,coupleId from member where delCheck='N' LIMIT #{param1},10")
	ArrayList<memberDTO> memberList(int page);

	@Select("select count(id) from member")
	int total();

	@Select("select admin from member where id = #{param1}")
	String admincheck(String id);

	@Update("update member set admin = #{param1} where id = #{param2}")
	void change(String chk, String id);

	@Select("select count(id) from report")
	int reportTotal();

	@Select("select * from report LIMIT #{param1},10")
	ArrayList<reportDTO> reportMemberList(int page);
	
	@Select("select * from report where reportNum = #{param1}")
	ArrayList<reportDTO> reportMemberDetail(int reportNum);

	@Update("update report set answer=#{param1}, status='Y' where reportNum=#{param2}")
	void answerEnter(String answer, int reportNum);

	@Update("update member set delCheck='Y' where id=#{param1}")
	void bye(String id);

	@Update("update report set status='Y' where id=#{param1}")
	void status(String id);

	@Select("select * from member where delCheck='Y' LIMIT #{param1},10")
	ArrayList<memberDTO> byeList(int page);

	@Select("select count(id) from member where delCheck='Y'")
	int byeTotal();
	
	@Update("update member set delCheck='N' where id=#{param1}")
	void comeBack(String id);
	
}
