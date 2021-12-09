package com.gudi.best.logic.matching.controller;

import com.gudi.best.dto.ProFileDTO;
import com.gudi.best.logic.matching.mapper.RestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RestController
@RequiredArgsConstructor
public class MatchingRestController {

    private final RestMapper mapper;
    @GetMapping("/matchingProfile")
    public ProFileDTO matchingProfile(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        return mapper.matchingProfile(loginId);
    }
}
