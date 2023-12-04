package com.example.springsocial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {
    
    @GetMapping("/xx")
    public String getsmth(){
        System.out.println("hoklla");
        return "Springoo";
    }

     @GetMapping("/aa")
    public String getsmth1(){
        System.out.println("hoklla");
        return "Springoo authenticated";
    }
}
