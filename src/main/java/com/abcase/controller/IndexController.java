package com.abcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class IndexController {
    @Autowired
//    private ProjectService projectService;

    @RequestMapping("/welcome")
    public String index(Map<String, Object> model) {
        model.put("message", "jfkjsd");
        return "welcome";
    }
}
