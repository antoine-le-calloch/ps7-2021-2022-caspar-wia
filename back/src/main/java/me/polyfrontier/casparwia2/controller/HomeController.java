package me.polyfrontier.casparwia2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to PolyFrontier.\n\nThe API is up and running.\n\nIt is intended to be used by Caspar WIA2. It's access is strictly limited to the Caspar WIA2 team.";
    }
}
