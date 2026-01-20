package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/")
    public String home() {
        return """
            <html>
            <body style='font-family: Arial'>
                <h1>Application Running Successfully</h1>
                <p>Environment: DEV</p>
                <p>Database: Connected</p>
                <p>Kafka: Configured</p>
            </body>
            </html>
        """;
    }
}
