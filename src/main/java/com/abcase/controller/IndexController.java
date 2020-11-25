package com.abcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class IndexController {
    @Autowired
//    private ProjectService projectService;

    @RequestMapping("/init")
    public String index(Map<String, Object> model) {
        return "case";
    }
}
