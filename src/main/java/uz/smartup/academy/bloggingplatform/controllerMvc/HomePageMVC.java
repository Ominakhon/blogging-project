package uz.smartup.academy.bloggingplatform.controllerMvc;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageMVC {
    @GetMapping("/login")
    public String HomePageController(){
        return "index";
    }



}
