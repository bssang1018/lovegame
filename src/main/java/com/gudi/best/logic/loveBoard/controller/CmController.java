package com.gudi.best.logic.loveBoard.controller;

import com.gudi.best.logic.loveBoard.service.CmService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RestController
@Log4j2
@RequestMapping("/cm")
public class CmController {

    @Autowired
    CmService service;

    @PostMapping("/cmInsert")
    public HashMap<String, Object> cmInsert(HttpSession session, @RequestParam HashMap<String, String> params) {
        String loginId = (String) session.getAttribute("loginId");
        params.put("id", loginId);
        return service.cmInsert(params, loginId);
    }

    @GetMapping("cmList/{page}/{boardNum}")
    public HashMap<String, Object> cmList(@PathVariable int page, @PathVariable int boardNum, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        return service.cmList(page, boardNum, loginId);
    }

    @PostMapping("/cmUpdate")
    public HashMap<String, Object> cmUpdate(HttpSession session, @RequestParam HashMap<String, String> params) {
        String loginId = (String) session.getAttribute("loginId");
        return service.cmUpdate(params, loginId);
    }

    @PostMapping("/cmDelete")
    public HashMap<String, Object> cmDelete(HttpSession session, @RequestParam HashMap<String, String> params) {
        String loginId = (String) session.getAttribute("loginId");
        return service.cmDelete(params, loginId);
    }
}
