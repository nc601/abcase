package com.abcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class IndexController {
    @Autowired
//    private ProjectService projectService;

    @RequestMapping("/web")
    public String web(Map<String, Object> model) {
        return "case";
    }

    @RequestMapping("/inter")
    public String inter(Map<String, Object> model) {
        return "sysconfig";
    }
}
