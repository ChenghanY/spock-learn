package com.james.controller;


import com.james.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class HelloWorldController {

  private HelloWorldService service;

  @Autowired
  public void setService(HelloWorldService service) {
    this.service = service;
  }

  @RequestMapping("/")
  public String hello() {
    return service.getHelloMessage();
  }

}
