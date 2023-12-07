package com.example.springsocial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {
    

    /// just to test methods
    
    @GetMapping("/xx")
    public String getsmth(){
        System.out.println("hoklla");
        return "Springoo - this endpoint is not authenticated";
    }

     @GetMapping("/aa")
    public String getsmth1(){
        System.out.println("hoklla");
        return "Springoo authenticated endpoint";
    }
}
