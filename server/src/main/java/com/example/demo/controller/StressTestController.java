package com.example.demo.controller;


import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domains.cards.CardsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/cards/")
@RequiredArgsConstructor
public class StressTestController {

    private static final Logger log = LoggerFactory.getLogger(StressTestController.class);
    private final CardsService cardsService; 

    @PostMapping
    public String loadTest(){
        log.info("Attempting to trigger load tests.");

        //code to execute tests.
        cardsService.loadTest();    

        log.info("Successfully triggered load tests");
        return Strings.EMPTY;

    }

}
