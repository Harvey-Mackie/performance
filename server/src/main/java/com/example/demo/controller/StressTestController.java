package com.example.demo.controller;


import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/team-name/")
public class StressTestController {

    private static final Logger log = LoggerFactory.getLogger(StressTestController.class);

    @GetMapping
    public String getAllTests(){
        return Strings.EMPTY;
    }

    @PostMapping
    public String loadTest(){
        log.info("Attempting to trigger load tests.");

        //code to execute tests.

        log.info("Successfully triggered load tests")
        return Strings.EMPTY;

    }

}
