package com.gudi.best.logic.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gudi.best.dto.memberDTO;
import com.gudi.best.dto.reportDTO;
import com.gudi.best.logic.admin.mapper.AdminMapper;
import com.gudi.best.logic.admin.service.AdminService;

import ch.qos.logback.classic.Logger;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired AdminService service;
	@Autowired AdminMapper mapper;
	
	@GetMapping("/main")
    public String main(Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = service.memberList(1);
		model.addAttribute("map", map);
        return "logic/admin/main";
    }
	
	@GetMapping("/main/{page}")
    public String mainPage(Model model, @PathVariable int page) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = service.memberList(page);
		model.addAttribute("map", map);
        return "logic/admin/main";
    }
	
	@ResponseBody
	@GetMapping("/change")
    public HashMap<String,Object> change(String id) {
		//System.out.println("실행 : "+id);
	 String chk = service.change(id);
	 HashMap<String, Object> map = new HashMap<String, Object>();
	 map.put("chk", chk );
	 System.out.println(map);
		return map;
    }
	
	@GetMapping("/reportMember/{page}")
    public String reportMember(Model model, @PathVariable int page) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = service.reportMemberList(page);
		model.addAttribute("map", map);
        return "logic/admin/reportMember";
    }
	
	@GetMapping("/reportMemberDetail/{reportNum}")
    public String reportMemberDetail(Model model, @PathVariable int reportNum) {
		ArrayList<reportDTO> list = service.reportMemberDetail(reportNum);
		log.info(list);
		model.addAttribute("map", list);
        return "logic/admin/reportMemberDetail";
    }
	
	@ResponseBody
	@PostMapping("/answerEnter")
    public int answerEnter(@RequestParam HashMap<String, String> param) {
		log.info(param);
        return service.answerEnter(param);
    }
	
	@ResponseBody
	@PostMapping("/bye")
    public String bye(@RequestParam HashMap<String, String> param) {
        return service.bye(param);
    }
	
	@GetMapping("/byeList/{page}")
    public String byeList(@PathVariable int page, Model model) {
		HashMap<String, Object> map = service.byeList(page);
		model.addAttribute("map", map);
        return "logic/admin/byeList";
    }
	
	@GetMapping("/comeBack/{id}")
    public String comeBack(@PathVariable String id, Model model) {
		mapper.comeBack(id);
		HashMap<String, Object> map = service.byeList(1);
		model.addAttribute("map", map);
        return "logic/admin/byeList";
    }
	
}
