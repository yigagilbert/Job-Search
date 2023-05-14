package org.launchcode.jobsearchtracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping
    public String displayRoot() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String displayLoginPage() {
        return "login";
    }
}

