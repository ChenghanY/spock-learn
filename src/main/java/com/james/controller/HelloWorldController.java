package com.james.controller;


import com.james.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Spring-MVC controller class.
 */
@RestController
public class HelloWorldController {

  @Autowired
  private HelloWorldService service;

  @RequestMapping("/")
  public String hello() {
    return service.getHelloMessage();
  }

}
