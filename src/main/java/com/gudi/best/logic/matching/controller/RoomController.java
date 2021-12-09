package com.gudi.best.logic.matching.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.gudi.best.logic.matching.dto.ChatDTO;
import com.gudi.best.logic.matching.dto.ChatRoomDTO;
import com.gudi.best.logic.matching.mapper.ChatMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Log4j2
public class RoomController {
	
	private final ChatMapper chatMapper;
	
	     //채팅방 개설
		@RequestMapping("/roomCreate")
		public String roomCreate(@RequestParam String person, HttpSession session, RedirectAttributes rttr, Model model) {
			String loginId = (String) session.getAttribute("loginId");
			log.info("# 채팅방 개설 요청... , loginID, roomName: " + loginId + " / " + person + " / ");
				
			//동일한 방 생성불가능
			//상대방 아이디가 멤버에 존재하지 않는 경우도 방생성 불가능
			//나-상대방, 상대방-나 => 두개의 방이 아니라, 하나의 방으로 만들어야함
			//내가 나한테 채팅방 생성도 불가능
			if(chatMapper.findOverlap(loginId, person) != null || chatMapper.findPerson(person)==null || loginId.equals(person)) {
				log.info("***방생성 불가능!!***");
				rttr.addFlashAttribute("loginId" , loginId);
				rttr.addFlashAttribute("errorMsg" , "상대방이 존재하지 않는 아이디이거나, 이미 생성된 채팅방 입니다.\r(본인의 아이디를 사용해서 생성이 불가능합니다.)");
				return "redirect:/chat/rooms";
			}else{
				log.info("***방생성 쌉가능!!***");
				chatMapper.createRoom(loginId,person);
				rttr.addFlashAttribute("loginId" , loginId);
				return "redirect:/chat/rooms";
				}
			}
		
		//채팅방 삭제
		@GetMapping("/roomDelete")
		public String roomDelete(String roomNum, HttpSession session, RedirectAttributes rttr, Model model) {
			log.info("# 채팅방 삭제 요청... roomNum :: " + roomNum);
			String loginId = (String) session.getAttribute("loginId");
			chatMapper.deleteChat(roomNum);
			chatMapper.deleteRoom(roomNum);
			rttr.addFlashAttribute("loginId" , loginId);
			return "redirect:/chat/rooms";
		}

	
	//채팅방 목록 조회
	@GetMapping("/rooms")
	public ModelAndView rooms(HttpServletRequest request, HttpSession session) {
		log.info("채팅방 목록 조회...");
		ModelAndView mav = new ModelAndView("chat/chatMain");
		
		//redirect 로 진입시
		if(RequestContextUtils.getInputFlashMap(request) != null) {
			Map<String, ?> reqMap = RequestContextUtils.getInputFlashMap(request);
			String loginId = (String) reqMap.get("loginId");	
			List<ChatRoomDTO> result = chatMapper.findAllRooms(loginId);
			mav.addObject("list", result);
			mav.addObject("loginId", loginId);
			return mav;
		//url 로 진입시
		} else {
			String loginId = (String) session.getAttribute("loginId");
			List<ChatRoomDTO> result = chatMapper.findAllRooms(loginId);
			mav.addObject("list", result);
			mav.addObject("loginId", loginId);
			return mav;
		}
	}
	

	//개인 채팅방 들어가기
	@GetMapping("/room")
	public String getRoom(String roomNum, Model model, HttpSession session, HttpServletRequest request) {
		log.info("채팅방 연결 요청... \n roomNum :: " + roomNum );
		
		String loginId = (String) session.getAttribute("loginId");
		model.addAttribute("loginId", loginId);
		
		//왼쪽에 채팅방 리스트 뿌려주기용
		List<ChatRoomDTO> result = chatMapper.findAllRooms(loginId);
		model.addAttribute("list", result);
		
		//드롭다운메뉴로 연결할 경우
		if(roomNum == null && RequestContextUtils.getInputFlashMap(request) != null) {
			Map<String, ?> reqMap = RequestContextUtils.getInputFlashMap(request);
			roomNum = (String) reqMap.get("roomNum");
		}
		
		//채팅룸 정보
		ChatRoomDTO dto = chatMapper.findRoomByNum(roomNum);
		log.info("dto 속을보자::" + dto);;
		model.addAttribute("room",dto);
		
		//최신 채팅 100개 불러오기
		chatMapper.initRownum(); //로우넘 초기화
		ChatDTO test = chatMapper.getMaxRownum(roomNum); //맥스로우넘 구해오기
		int maxRowNum = 0;
		if(test != null) {
			maxRowNum = test.getMaxRowNum();		
		}
		chatMapper.initRownum(); //로우넘 초기화2
		List<ChatDTO> chatDTO =  chatMapper.chatFind(roomNum, maxRowNum);
		log.info("List ChatDTO 의 형태를 보자!! :: " + chatDTO);
		model.addAttribute("chatDTO", chatDTO);
		
		//읽었는지 안읽었는지 구분하기 위해서 readState 값을 'n' -> 'y' 로 업뎃해주자
		chatMapper.readStateUpdate(roomNum);
		
		return "chat/chatSelect";
	}
	
	//아이디로 채팅 연결
	@GetMapping("/chatCon")
	public String chatCon(HttpSession session, String person) {
		log.info("드롭바를 통해서 채팅방 연결 요청...");
		String loginId = (String) session.getAttribute("loginId");
		
		if(chatMapper.findOverlap(loginId, person)==null) {
			//아직 방이 생성되지 않은 경우는 방 생성후 이동
			log.info("생성된 방이 없으므로 roomCreate 로 요청...");
			return "redirect:/chat/roomCreate?person="+person;
		}else{
			//이미 방이 존재하는 경우는 바로 이동
			log.info("생성된 방이 있으니까 room 으로 요청...");
			return "redirect:/chat/room?roomNum="+ chatMapper.findOverlap(loginId, person).getRoomNum().toString();
		}
	}
	
	//아이디 검색 아작스
	@ResponseBody
	@GetMapping("/searchId")
	public HashMap<String, Object> search(String id, HttpSession session) {
		ArrayList<String> list = chatMapper.searchId(id);
		System.out.println("아이디 검색리스트 :: "+list);
		
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
	
}
