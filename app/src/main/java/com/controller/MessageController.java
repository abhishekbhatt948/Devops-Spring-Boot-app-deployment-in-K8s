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
