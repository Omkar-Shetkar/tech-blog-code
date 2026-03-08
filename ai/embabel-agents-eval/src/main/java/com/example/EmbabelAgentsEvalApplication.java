package com.example;

import com.embabel.agent.config.annotation.EnableAgents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmbabelAgentsEvalApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmbabelAgentsEvalApplication.class, args);
    }
}

//record EssayResponse(String title, String content) {}
//
//@RestController
//class EssayController {
//
//    @GetMapping("/essay")
//    public EssayResponse getEssay() {
//        return new EssayResponse(title, content);
//    }
//
//}

