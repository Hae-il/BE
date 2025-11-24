package com.haeil.full.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/client")
@Controller
@RequiredArgsConstructor
public class ClientController {

    @GetMapping
    public String clientList(Model model) {
        return "client/list";
    }
}
