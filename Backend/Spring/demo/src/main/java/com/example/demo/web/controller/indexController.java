package com.example.demo.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class indexController {

    @GetMapping("/index")
    public ModelAndView mostrarIndex() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }
    
}
