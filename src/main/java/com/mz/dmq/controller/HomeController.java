package com.mz.dmq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}