package com.gudi.best.logic.matching.controller;

import javax.servlet.http.HttpSession;

import com.gudi.best.logic.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService service;

    @GetMapping("/matchingMain")
    public String matchingMain(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("list", service.matchingHobby(loginId));
        return "logic/matching/matchingMain";
    }

    //===================

    @GetMapping("/chat")
    public String chatGET(HttpSession session, Model model) {
        log.info("@MatchingController, chatGET()");
        String loginId = (String) session.getAttribute("loginId");
        log.info("현재 로그인 ID :: " + loginId);
        model.addAttribute("loginId", loginId);
        return "chat/chatMain";
    }


}
