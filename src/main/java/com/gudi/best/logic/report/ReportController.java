package com.gudi.best.logic.report;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@Log4j2
public class ReportController {

    @Autowired
    ReportMapper mapper;

    @GetMapping("/report")
    public String reportForm(String id, Model model) {
        model.addAttribute("id", id);
        return "logic/report/reportForm";
    }

    @PostMapping("/report")
    public String report(@RequestParam HashMap<String, String> params, HttpSession session) {
        params.put("reporter", (String) session.getAttribute("loginId"));
        mapper.report(params);
        return "redirect:/loveBoard/list/all/1";
    }
}
