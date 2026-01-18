package com.example.demo.controller;

import com.example.demo.service.KafkaProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final KafkaProducerService producer;

    public MessageController(KafkaProducerService producer) {
        this.producer = producer;
    }

    @PostMapping("/publish")
    public String publish(@RequestBody String msg) {
        producer.sendMessage(msg);
        return "Message sent to Kafka";
    }
}
