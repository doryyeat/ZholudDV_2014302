package com.bookland.controller;

import com.bookland.controller.main.Main;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexCont extends Main {

    @GetMapping
    public String index1() {
        return "redirect:/books";
    }

    @GetMapping("/index")
    public String index2() {
        return "redirect:/books";
    }

}
