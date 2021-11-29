package br.com.sicredi.events;

import br.com.sicredi.models.Pauta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPautaCreateEvent {
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${message.topic.pautas}")
    private String topicName;

    public KafkaPautaCreateEvent(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicEvent(Pauta pauta) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var json = objectWriter.writeValueAsString(pauta);
        kafkaTemplate.send(topicName,json);
    }
}
