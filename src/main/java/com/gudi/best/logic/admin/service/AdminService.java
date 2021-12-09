package com.gudi.best.logic.admin.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gudi.best.dto.memberDTO;
import com.gudi.best.dto.reportDTO;
import com.gudi.best.logic.admin.mapper.AdminMapper;
import com.gudi.best.util.PageNation;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AdminService {

	@Autowired AdminMapper mapper;

	public HashMap<String, Object> memberList(int page) {
		int total = mapper.total();
		HashMap<String, Object> map = PageNation.pagination(page, 10, total);
		if(page==1) {
			page =0;
		}else {
			page = (page - 1) * 10 ;
		}
		ArrayList<memberDTO> list = mapper.memberList(page);
		map.put("list", list);
		return map;
	}

	public String change(String id) {
		 String chk = mapper.admincheck(id);
		 if(chk.equals("Y")) {
			 chk = "N";
			 mapper.change(chk,id);
		 } else {
			 chk = "Y";
			 mapper.change(chk,id);
		 }
		 return chk;
	}


	public HashMap<String, Object> reportMemberList(int page) {
		int total = mapper.reportTotal();
		HashMap<String, Object> map = PageNation.pagination(page, 10, total);
		if(page==1) {
			page =0;
		}else {
			page = (page - 1) * 10 ;
		}
		ArrayList<reportDTO> list = mapper.reportMemberList(page);
		map.put("list", list);
		return map;
	}

	public ArrayList<reportDTO> reportMemberDetail(int reportNum) {
		ArrayList<reportDTO> list = mapper.reportMemberDetail(reportNum);
		return list;
	}

	@Transactional
	public int answerEnter(HashMap<String, String> param) {
		int reportNum = Integer.parseInt(param.get("rNum"));
		String answer = param.get("answer");
		mapper.answerEnter(answer, reportNum);
		return reportNum;
		
	}

	@Transactional
	public String bye(HashMap<String, String> param) {
		String id = param.get("id");
		mapper.bye(id);
		mapper.status(id);
		return "정지";
	}

	public HashMap<String,Object> byeList(int page) {
		int total = mapper.byeTotal();
		HashMap<String, Object> map = PageNation.pagination(page, 10, total);
		if(page==1) {
			page =0;
		}else {
			page = (page - 1) * 10 ;
		}
		ArrayList<memberDTO> list = mapper.byeList(page);
		map.put("list", list);
		return map;
	}
	
	
}
