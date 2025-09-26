package com.simplecoding.cheforest.jpa.dust.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DustViewController {

    @GetMapping("/dust/view")
    public String dustView() {
        return "dust/dust"; // => /WEB-INF/views/dust.jsp
    }
}
