package adapter.infrastructure;

import domain.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer implements Producer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private final String TOPIC;
    private final KafkaTemplate<String, String> template;

    public KafkaProducer(@Value("${spring.kafka.template.default-topic}") String topic, KafkaTemplate<String, String> template) {
        TOPIC = topic;
        this.template = template;
    }

    @Override
    public void publish(String message) {
        try {
            template.send(TOPIC, message);
            StringBuilder logMessage = new StringBuilder("Send to queue: '...");
            logMessage.append(message.substring(message.length() - 7));
            logger.info(logMessage.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
