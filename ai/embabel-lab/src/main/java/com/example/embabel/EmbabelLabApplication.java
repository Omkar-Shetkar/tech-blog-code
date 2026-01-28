package com.example.embabel;

import com.embabel.agent.api.common.Ai;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
public class EmbabelLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmbabelLabApplication.class, args);
    }

}

@RestController
class SampleController {

    private final Ai ai;

    public SampleController(Ai ai) {
        this.ai = ai;
    }

    @GetMapping("/api")
    public ResponseEntity<CustomerQuery> customerQuery(@RequestParam String query) {
        CustomerQuery customerQuery = ai.withAutoLlm()
                .createObject("""
                        Customer query: %s
                        """.formatted(query), CustomerQuery.class);
        return ResponseEntity.ok(customerQuery);

    }

    public record CustomerQuery(String customerName, String oderId, String issue) {
    }

}
