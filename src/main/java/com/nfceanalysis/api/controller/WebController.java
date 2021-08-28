package com.nfceanalysis.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @RequestMapping("/api")
    @ResponseBody
    public String index() {
        return "That's pretty basic!";
    }
}
