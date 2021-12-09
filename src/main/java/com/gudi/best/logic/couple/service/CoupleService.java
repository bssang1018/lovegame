package com.gudi.best.logic.couple.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gudi.best.dto.CalenderDTO;
import com.gudi.best.logic.couple.mapper.CoupleMapper;
import com.gudi.best.util.PageNation;
import com.gudi.best.util.S3Uploader;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class CoupleService {

	@Autowired CoupleMapper mapper;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	CalenderDTO Cdto = new CalenderDTO();
	
	@Autowired
    S3Uploader uploader;
	
	@Transactional
	public void calenderEnter(String id, HashMap<String, String> param) {
		
		Cdto.setId(id);
		Cdto.setStart(param.get("start"));
		Cdto.setEnd(param.get("end"));
		Cdto.setTitle(param.get("title"));
		Cdto.setContent(param.get("content"));
		Cdto.setColor(param.get("color"));
		mapper.calenderEnter(Cdto);
	}

	public HashMap<String,Object> readCalender(String id) {
		//Cdto.setId(id);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<CalenderDTO> list = mapper.readCalender(id);
		 map.put("list", list );
		return map;
	}
	

	public ModelAndView detail(String id, int cnum) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, String> map = mapper.detail(id,cnum);
		map.put("sTime", map.get("start").substring(11,16));
		map.put("start",map.get("start").substring(0,10));
		if(map.get("end").length()>10) {
			map.put("eTime", map.get("end").substring(11,16));
			map.put("end",map.get("end").substring(0,10));
		}
		//System.out.println(map);
		mav.addObject("map", map);
		mav.setViewName("logic/couple/calenderDetail");
		return mav;
		
	}

	public ModelAndView updateForm(String id, int cnum) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, String> map = mapper.detail(id,cnum);
		map.put("sTime", map.get("start").substring(11,16));
		map.put("start",map.get("start").substring(0,10));
		if(map.get("end").length()>10) {
			map.put("eTime", map.get("end").substring(11,16));
			map.put("end",map.get("end").substring(0,10));
		}
		System.out.println(map);
		mav.addObject("map", map);
		mav.setViewName("logic/couple/calenderUpdateForm");
		return mav;
	}
	
	@Transactional
	public void calenderUpdate(String id, HashMap<String, String> param) {
		Cdto.setCNum(Integer.parseInt(param.get("Cnum")));
		Cdto.setId(id);
		Cdto.setStart(param.get("start"));
		Cdto.setEnd(param.get("end"));
		Cdto.setTitle(param.get("title"));
		Cdto.setContent(param.get("content"));
		Cdto.setColor(param.get("color"));
		mapper.calenderUpdate(Cdto);
		
	}

	public void calenderDel(String id, int cnum) {
		mapper.calenderDel(id,cnum);
		System.out.println("일정 삭제");
		
	}


	public HashMap<String, Object> readMemory(String id) {
		String chk = mapper.coupleChk(id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(mapper.historyTotal(id)>0) {
			map.put("history", "Y");
		};
		ArrayList<CalenderDTO> list = mapper.readMomory(id,chk);
		map.put("list", list);
		map.put("chk", "Y");
		map.put("couple", chk);
		if(chk.equals("없음")) {
		map.put("chk", "N");
		} else if(chk.length()>9) {
			if(chk.substring(0, 5).equals("Apply")) {
				map.put("chk", "A"); //상대방의 응답을 기다립니다 보여주기
			}	else if(chk.substring(0, 8).equals("Response")) {
				map.put("chk", "R"); // 요청자의 프로필과 수락창 보여주기 -> 만들예정
			}
			}
			
		return map;
	}
	
	
	public HashMap<String, Object> readMemoryP(String id, int page) {
		String chk = mapper.coupleChk(id);
		int total = mapper.totalMemory(id, chk);
		HashMap<String, Object> map = PageNation.pagination(page, 10, total);
		if(page==1) {
			page =0;
		}else {
			page = (page - 1) * 10 ;
		}
		
		ArrayList<CalenderDTO> list = mapper.readMomoryP(id,chk,page);
		map.put("list", list);
		map.put("chk", "Y");
		map.put("couple", chk);
		
		if(chk.equals("없음")) {
		map.put("chk", "N");
		} else if(chk.length()>9) {
			if(chk.substring(0, 5).equals("Apply")) {
				map.put("chk", "A"); //상대방의 응답을 기다립니다 보여주기
			}	else if(chk.substring(0, 8).equals("Response")) {
				map.put("chk", "R"); // 요청자의 프로필과 수락창 보여주기 -> 만들예정
			}
			
			};
			
			
		return map;
	}

	public void applyCouple(String Lid, String id) {
		String Aid = "Apply"+ id; //상대방에게 요청 apply+상대방 id
    	String Rid = "Response"+Lid; //요청보낸사람에게 응답 response+요청자id
    	String p1 = Aid;
    	String p2 = Lid;
		mapper.applyCouple(p1, p2);
		p1 = Rid;
		p2 = id;
		mapper.applyCouple(p1, p2);
	}

	public void choiceApply(String id, String ok) {
		String Rid = mapper.coupleChk(id);
		if(ok.equals("N")) {
		String p1 = "없음";
		String p2 = id;
		mapper.applyCouple(p1, p2);
		if(Rid.substring(0, 5).equals("Apply")) {
			p2 = Rid.substring(5);
		}else if(Rid.substring(0, 8).equals("Response")) {
			p2 = Rid.substring(8);
		}else {
			p2 = Rid;
		}
		mapper.applyCouple(p1, p2);
		}else if(ok.equals("Y")) {
			String p1 = Rid.substring(8);
			String p2 = id;
			System.out.println(p1+ " :" + p2);
			mapper.applyCouple(p1, p2);
			p1 = id;
			p2 = Rid.substring(8);
			mapper.applyCouple(p1, p2);	
		}
	}

	@Transactional
	public int memoryWrite(HashMap<String, String> param, MultipartFile[] files, String id) {
		Cdto.setId(id);
		Cdto.setStart(param.get("start"));
		Cdto.setEnd(param.get("end"));
		Cdto.setTitle(param.get("title"));
		Cdto.setContent(param.get("content"));
		Cdto.setColor(param.get("color"));
		mapper.memoryWrite(Cdto);
		 int cNum = Cdto.getCNum();
		photoUpload(files, cNum, id);
		return cNum;
	}

    // 사진 업로드 및 포토 테이블 삽입 메서드
	@Transactional
	public void photoUpload(MultipartFile[] files, int cNum, String id) {
        if (files != null) {
            // 파일 업로드 하기
            ArrayList<HashMap<String, Object>> imgMapList = new ArrayList<HashMap<String, Object>>();
            for (MultipartFile file : files) {
                try {
                    HashMap<String, Object> map = uploader.upload(file);
                    map.put("id", id);
                    map.put("divisionNum", cNum);
                    imgMapList.add(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 포토테이블에 값 넣기
            for (HashMap<String, Object> map : imgMapList) {
               mapper.photoInsert(map);
            	//log.info(map);
            }
        }
    }


	public HashMap<String, Object> memoryDetail(int cNum) {
		HashMap<String, Object> map = mapper.memoryDetail(cNum);
		if(((String) map.get("start")).length()>10) {
		map.put("sTime", ((String) map.get("start")).substring(11,16));
		map.put("start",((String)map.get("start")).substring(0,10));
		}
		if(((String) map.get("end")).length()>10) {
			map.put("eTime", ((String) map.get("end")).substring(11,16));
			map.put("end",((String) map.get("end")).substring(0,10));
		}
		 map.put("photoList", mapper.boardPhoto(cNum));
	     map.put("photoCount", mapper.photoCount(cNum));
		return map;
	}


@Transactional
public int imgDel(String newFileName) {
    uploader.delete(newFileName);
   int cNum =mapper.divisionNum(newFileName);
   mapper.photoDel(newFileName);
   int photoCount = mapper.photoCount(cNum);
   return photoCount;
}

public int memoryUpdate(HashMap<String, String> param, MultipartFile[] files, String id) {
	Cdto.setCNum(Integer.parseInt(param.get("cNum")));
	Cdto.setId(id);
	Cdto.setStart(param.get("start"));
	Cdto.setEnd(param.get("end"));
	Cdto.setTitle(param.get("title"));
	Cdto.setContent(param.get("content"));
	Cdto.setColor(param.get("color"));
	mapper.memoryUpdate(Cdto);
	 int cNum = Cdto.getCNum();
	photoUpload(files, cNum, id);
	return cNum;
}

public void memoryDel(int cNum) {
	ArrayList<String> list = mapper.photoName(cNum);
	for (String newFileName : list) {
	uploader.delete(newFileName);
	 mapper.photoDel(newFileName);
	}
	mapper.memoryDel(cNum);
}


}