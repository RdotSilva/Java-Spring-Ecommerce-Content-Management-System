package com.rdotsilva.cms.ecommerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Instead of setting the Mapping in the controller I created a WebConfig and set the view controller
    // within the WebConfig file. (This is good if you only want to return a view)
    @GetMapping("/someRandomPage")
    public String home() {
        return "home";
    }
}
