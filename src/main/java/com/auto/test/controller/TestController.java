package com.auto.test.controller;

import com.auto.test.service.DemoService;
import com.auto.test.standard.StdJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("demo")
    public StdJson autoBooking() throws Exception {
        return StdJson.ok(demoService.demoActions());
    }

}
