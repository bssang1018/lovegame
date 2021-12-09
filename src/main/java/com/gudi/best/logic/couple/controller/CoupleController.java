package com.gudi.best.logic.couple.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import com.gudi.best.dto.CalenderDTO;
import com.gudi.best.dto.ProFileDTO;
import com.gudi.best.logic.couple.mapper.CoupleMapper;
import com.gudi.best.logic.couple.service.CoupleService;
import com.gudi.best.logic.myInfo.service.MyInfoService;
import com.gudi.best.util.PageNation;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/couple")
public class CoupleController {
	
	private final CoupleMapper mapper;
	private final CoupleService service;
	
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/loveCalender")
	public String loveCalender(HttpSession session) {
		return "logic/couple/loveCalender";
	}

	@GetMapping("/readCalender")
	@ResponseBody
	public HashMap<String, Object> readCalender(HttpSession session) {
		String id = (String) session.getAttribute("loginId");
		return service.readCalender(id);
	}
	
	@GetMapping("/readMemory")
	@ResponseBody
	public HashMap<String, Object> readMemory(HttpSession session) {
		String id = (String) session.getAttribute("loginId");
		return service.readMemory(id);
	}

	@PostMapping("/loveCalenderEnterForm")
	public String loveCalenderEnterForm(@RequestParam Date start, @RequestParam Date end, HttpSession session,
			Model model) {
		String id = (String) session.getAttribute("loginId");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		logger.info(format.format(start) + "~" + format.format(end));
		model.addAttribute("start", format.format(start));
		model.addAttribute("end", format.format(end));
		return "logic/couple/loveCalenderEnter";
	}

	@PostMapping("/loveCalenderEnter")
	public String loveCalenderEnter(@RequestParam HashMap<String, String> param, HttpSession session) {
		System.out.println("입력데이터" + param);
		String id = (String) session.getAttribute("loginId");
		if (param.get("sTime") != null) {
			param.put("start", param.get("start") + "T" + param.get("sTime"));
			 System.out.println(param.get("start"));
		}
		if (param.get("eTime") != null && param.get("eTime") != "") {
			param.put("end", param.get("end") + "T" + param.get("eTime"));
			System.out.println(param.get("end"));
		}
		service.calenderEnter(id, param);
		return "logic/couple/loveCalender";

	}

	@GetMapping("/CalenderDetail/{cnum}")
	public ModelAndView CalenderDetail(HttpSession session, @PathVariable int cnum) {
		String id = (String) session.getAttribute("loginId");
		return service.detail(id, cnum);
	}

	@GetMapping("/calenderUpdateForm/{cnum}")
	public ModelAndView calenderUpdateForm(HttpSession session, @PathVariable int cnum) {
		String id = (String) session.getAttribute("loginId");
		return service.updateForm(id, cnum);
	}

	@PostMapping("/calenderUpdate")
	public String calenderUpdate(@RequestParam HashMap<String, String> param, HttpSession session) {
		//System.out.println("업데이트 실행");
		System.out.println("파라미터 값" + param);
		String id = (String) session.getAttribute("loginId");
		if (param.get("sTime") != null) {
			param.put("start", param.get("start") + "T" + param.get("sTime"));
			// System.out.println(param.get("start"));
		}
		if (param.get("eTime") != null && param.get("eTime") != "") {
			param.put("end", param.get("end") + "T" + param.get("eTime"));
			 System.out.println(param.get("end"));
		}
		service.calenderUpdate(id, param);
		return "redirect:/couple/CalenderDetail/" + param.get("Cnum");

	}

	@GetMapping("/calenderDel/{cnum}")
	public String calenderDel(HttpSession session, @PathVariable int cnum) {
		String id = (String) session.getAttribute("loginId");
		service.calenderDel(id, cnum);
		return "redirect:/couple/loveCalender";
	}

	@GetMapping("/loveMemory/{page}")
	public String loveMemory(HttpSession session, Model model, @PathVariable int page) {
		String id = (String) session.getAttribute("loginId");
		HashMap<String, Object> map = service.readMemoryP(id,page);
		model.addAttribute("map", map);
		log.info(map);
		return "logic/couple/loveMemory";
	}
	
	

	@GetMapping("/addCoupleIdForm")
	public String addCoupleIdForm() {
		return "logic/couple/addCoupleIdForm";
	}

	@ResponseBody
	@GetMapping("/search")
	public HashMap<String, Object> search(String id, HttpSession session) {
		ArrayList<String> list = mapper.search(id);
		System.out.println(list);
		/* for (String i : list) { if(i.equals(id)) { list.remove(i); } } */
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			if (list.get(i).equals(session.getAttribute("loginId"))) {
				list.remove(i);
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", list);
		System.out.println(map);
		return map;
	}

	@Autowired
	MyInfoService myInfoService;

	@GetMapping("/searchF")
	public String searchF(String id, Model model, HttpSession session) {
		ArrayList<String> list = mapper.searchF(id);
		System.out.println(list + "결과");
		if (list.isEmpty()) {
			model.addAttribute("id", "N"); // display에 검색결과 없음 처리해주기 id가 N이면
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals((String) session.getAttribute("loginId"))) {
					list.remove(i);
				}
			}
			if (list.isEmpty()) {
				model.addAttribute("id", "N");
			} else {
				ProFileDTO dto = myInfoService.proFileDetail(id);
				if (dto != null) {
					if (StringUtils.isEmpty(dto.getImgPath())) {
						dto.setImgPath("/img/noImg.png");
					}
					model.addAttribute("id", "Y");
					model.addAttribute("search", list);
					model.addAttribute("dto", dto);
					System.out.println(dto);
				}
			}
		}
		return "logic/couple/display";
	}

	@GetMapping("/display/{id}")
	public String display(@PathVariable String id, Model model) {
		System.out.println("id확인 : " + id);
		ArrayList<String> list = mapper.searchF(id);
		ProFileDTO dto = myInfoService.proFileDetail(id);
		if (dto != null) {
			if (StringUtils.isEmpty(dto.getImgPath())) {
				dto.setImgPath("/img/noImg.png");
			}
		}
		model.addAttribute("id", "Y");
		model.addAttribute("search", list.get(0));
		model.addAttribute("dto", dto);
		// HashMap<String, Object> map = mapper.display(id);
		// model.addAttribute("id", map);
		return "logic/couple/display";
	}

	@GetMapping("/applyCouple")
	public String applyCouple(String id, Model model, HttpSession session) {
		if (id.equals("") || id == null) {
			return "logic/couple/addCoupleIdForm";
		} else {
			String Lid = (String) session.getAttribute("loginId");
			service.applyCouple(Lid, id);
			HashMap<String, Object> map = service.readMemory(Lid);
			model.addAttribute("map", map);
			return "logic/couple/loveMemory";
		}
	}

	@GetMapping("/cancelApply")
	public String cancelApply(Model model, HttpSession session) {
		String id = (String) session.getAttribute("loginId");
		String ok = "N";
		service.choiceApply(id, ok);
		HashMap<String, Object> map = service.readMemory(id);
		model.addAttribute("map", map);
		return "logic/couple/loveMemory";
	}

	@GetMapping("/acceptApply")
	public String acceptApply(Model model, HttpSession session) {
		String id = (String) session.getAttribute("loginId");
		String ok = "Y";
		service.choiceApply(id, ok);
		HashMap<String, Object> map = service.readMemory(id);
		model.addAttribute("map", map);
		return "logic/couple/loveMemory";
	}
	
	@GetMapping("/memoryWriteFormC")
	public String memoryWriteForm(@RequestParam Date start, @RequestParam Date end, HttpSession session,Model model) {
		if(start !=null || end !=null) {
		String id = (String) session.getAttribute("loginId");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			logger.info(format.format(start) + "~" + format.format(end));
			model.addAttribute("start", format.format(start));
			model.addAttribute("end", format.format(end));
		}
			return "logic/couple/memoryWriteForm";
	}
	
	@GetMapping("/memoryWriteForm")
	public String memoryWriteForm() {
			return "logic/couple/memoryWriteForm";
	}
	
	@ResponseBody
	@PostMapping("/memoryWrite")
	public int memoryWrite(@RequestParam HashMap<String, String> param, HttpSession session, MultipartFile[] files) {
		String id = (String) session.getAttribute("loginId");
		if (param.get("sTime") != null) {
			param.put("start", param.get("start") + "T" + param.get("sTime"));
			// System.out.println(param.get("start"));
		}
		if (param.get("eTime") != null && param.get("eTime") != "") {
			param.put("end", param.get("end") + "T" + param.get("eTime"));
			// System.out.println(end);
		}
		int cNum = service.memoryWrite(param, files,id);
		return cNum;
	}
	
	@GetMapping("/memoryDetail/{cNum}")
	public String memoryDetail( @PathVariable int cNum, Model model,HttpSession session) {
		String id = (String) session.getAttribute("loginId");
        HashMap<String, Object> map = service.memoryDetail(cNum);
        if(map.get("id").equals(id)) {
        	map.put("writer", "Y");
        }else {
        	map.put("writer", "N");
        }
        model.addAttribute("map", map);
        log.info(map);
		return "logic/couple/memoryDetail";
	}
	
	
	@GetMapping("/memoryUpdateForm/{cNum}")
	public String memoryUpdateForm( @PathVariable int cNum, Model model) {
        HashMap<String, Object> map = service.memoryDetail(cNum);
        model.addAttribute("map", map);
        log.info(map);
		return "logic/couple/memoryUpdateForm";
	}
	
	@ResponseBody
	@PostMapping("/memoryUpdate")
	public int memoryUpdate(@RequestParam HashMap<String, String> param, HttpSession session, MultipartFile[] files) {
		log.info(param);
		String id = (String) session.getAttribute("loginId");
		if (param.get("sTime") != null) {
			param.put("start", param.get("start") + "T" + param.get("sTime"));
			// System.out.println(param.get("start"));
		}
		if (param.get("eTime") != null && param.get("eTime") != "") {
			param.put("end", param.get("end") + "T" + param.get("eTime"));
			// System.out.println(end);
		}
		log.info(param);
		int cNum = service.memoryUpdate(param, files,id);
		return cNum;
	}
	
	@ResponseBody
    @PostMapping("/imgDel")
    public int imgDel(@RequestParam HashMap<String, String> param, HttpSession session) {
		String newFileName = param.get("name");
		return  service.imgDel(newFileName);
    }
	
	@GetMapping("/memoryDel/{cNum}")
	public String memoryDel(@PathVariable int cNum) {
		service.memoryDel(cNum);
		return  "redirect:/couple/loveMemory";
    }
	
	@GetMapping("/history/{page}")
	public String history(@PathVariable int page,HttpSession session, Model model) {
		String id = (String) session.getAttribute("loginId");
		int total = mapper.historyTotal(id);
		HashMap<String, Object> map = PageNation.pagination(page, 10, total);
		if(page==1) {
			page =0;
		}else {
			page = (page - 1) * 10 ;
		}
		ArrayList<CalenderDTO> list = mapper.history(id,page);
		map.put("list", list);
		model.addAttribute("map", map);
		return  "logic/couple/historyMemory";
    }
}