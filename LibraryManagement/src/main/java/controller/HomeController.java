package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** トップメニュー */
    @GetMapping("/")
    public String home() {
        return "home";   // src/main/resources/templates/home.html
    }
}