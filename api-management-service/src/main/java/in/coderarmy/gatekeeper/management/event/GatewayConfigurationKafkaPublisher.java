package in.coderarmy.gatekeeper.management.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tools.jackson.databind.ObjectMapper;

@Component
public class GatewayConfigurationKafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public GatewayConfigurationKafkaPublisher(KafkaTemplate<String, String> kafkaTemplate,
                                              ObjectMapper objectMapper,
                                              @Value("${gateway.kafka.configuration-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(GatewayConfigurationChangeEvent event) {

        String key = event.getOrganizationId() + ":" +
                (event.getRouteId() == null ? "organization" : event.getRouteId());

        try {
            kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(event))
                    .whenComplete((result, exception) -> {});
        }
        catch(Exception exception) {
            System.out.println("Could not create Gateway configuration event " + event.getEventId());
        }
    }
}
