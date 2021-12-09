package com.gudi.best.logic.loveInfo.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudi.best.logic.loveInfo.mapper.LoveInfoMapper;
import com.gudi.best.util.NewApiUtil;
import com.gudi.best.util.PageNation;
import com.gudi.best.util.Scheduler;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/dateInfo")
public class LoveInfoController {
	@Autowired
	LoveInfoMapper loveInfoMapper;

	@GetMapping("/tourPick")
	public String tourPick() {
		return "logic/dateInfo/menu/tourPick";

	}

	@GetMapping("/tourMenu/{tour}")
	public String tourPick(@PathVariable String tour, Model model) {
		// System.out.println("지금 찍은 관광 코드는 : " +tour);
		model.addAttribute("tour", tour);
		return "logic/dateInfo/menu/locationPick";

	}

	@GetMapping("/locSpot/{loc}/{tour}/{page}")
	public String tourPick(@PathVariable String page, @PathVariable String loc, @PathVariable String tour, Model model)
			throws Exception {
		// System.out.println(loc+"="+tour);
		ArrayList<HashMap<String, Object>> list = NewApiUtil.culList(tour, loc, page);
		int lastIndex = list.size() - 1;
		String totalCount = list.get(lastIndex).get("totalCount").toString();
		HashMap<String, Object> map = PageNation.pagination(Integer.parseInt(page), 15, Integer.parseInt(totalCount));
		list.remove(lastIndex);
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		return "logic/dateInfo/menu/map";
	}

	@GetMapping("/infoDetail/{contentid}/{contenttypeid}")
	public String infoDetail(@PathVariable String contentid, @PathVariable String contenttypeid, Model model)
			throws Exception {
		HashMap<String, Object> map = NewApiUtil.culList(contentid, contenttypeid);
		model.addAttribute("map", map);
		return "logic/dateInfo/menu/detail";
	}

}
