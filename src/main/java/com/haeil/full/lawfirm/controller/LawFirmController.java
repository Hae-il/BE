package com.haeil.full.lawfirm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lawfirm")
public class LawFirmController {

    @GetMapping
    public String index() {
        return "lawfirm/index";
    }
}

