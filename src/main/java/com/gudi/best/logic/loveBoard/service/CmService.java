package com.gudi.best.logic.loveBoard.service;

import com.gudi.best.dto.CmDTO;
import com.gudi.best.logic.loveBoard.mapper.CmMapper;
import com.gudi.best.util.PageNation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CmService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CmMapper mapper;

    @Transactional
    public HashMap<String, Object> cmList(int page, int boardNum, String loginId) {
        int total = mapper.cmTotal(boardNum);
        int start = 0;
        HashMap<String, Object> map = PageNation.pagination(page, 8, total);
        if (page == 1) {
            start = 0;
        } else {
            start = (page - 1) * 8;
        }
        map.put("cmList", mapper.cmList(start, boardNum));
        map.put("loginId", loginId);
        return map;
    }

    @Transactional
    public HashMap<String, Object> cmInsert(HashMap<String, String> params, String loginId) {
        mapper.cmInsert(params);
        HashMap<String, Object> map = cmList(1, Integer.parseInt(params.get("divisionNum")), loginId);
        map.put("loginId", loginId);
        return map;
    }

    @Transactional
    public HashMap<String, Object> cmUpdate(HashMap<String, String> params, String loginId) {
        mapper.cmUpdate(params);
        int page = cmPageCheck(params.get("divisionNum"), Integer.parseInt(params.get("cmNum")));
        HashMap<String, Object> map = cmList(page, Integer.parseInt(params.get("divisionNum")), loginId);
        return map;
    }

    // 댓글이 몇 페이지에 있는가
    public int cmPageCheck(String divisionNum, int cmNum) {
        int page = 1;
        int start = 0;
        while (true) {
            start = (page - 1) * 8;
            String find = mapper.cmPageCheck(divisionNum, cmNum, start);
            if (find != null) {
                break;
            } else {
                page++;
            }
        }
        return page;
    }

    public HashMap<String, Object> cmDelete(HashMap<String, String> params, String loginId) {
        int page = cmPageCheck(params.get("divisionNum"), Integer.parseInt(params.get("cmNum")));
        mapper.cmDelete(params.get("cmNum"));
        HashMap<String, Object> map = cmList(page, Integer.parseInt(params.get("divisionNum")), loginId);
        ArrayList<CmDTO> cmList = (ArrayList<CmDTO>) map.get("cmList");
        if (cmList.size() < 1 && page != 1) {
            map = cmList(page - 1, Integer.parseInt(params.get("divisionNum")), loginId);
        }
        map.put("loginId", loginId);
        return map;
    }
}
