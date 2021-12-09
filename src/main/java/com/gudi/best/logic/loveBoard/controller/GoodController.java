package com.gudi.best.logic.loveBoard.controller;

import com.gudi.best.logic.loveBoard.service.GoodService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@Log4j2
public class GoodController {
    @Autowired
    GoodService service;

    @ResponseBody
    @GetMapping("/good/goodData/{boardNum}/{division}")
    public HashMap<String, Object> goodData(@PathVariable String division, @PathVariable String boardNum, HttpSession session) {
        String id = (String) session.getAttribute("loginId");
        return service.goodData(boardNum, id, division);
    }

}
