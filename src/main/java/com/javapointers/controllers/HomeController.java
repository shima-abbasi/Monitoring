package com.javapointers.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Jerry on 4/5/14.
 */
@Controller
public class HomeController {

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public String viewHome(){
        return "home";
    }

}
