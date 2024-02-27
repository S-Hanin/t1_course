package ru.khanin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import ru.khanin.dto.Phrase;

@RequiredArgsConstructor
public class PhrasePublisher {

	@Value("${external.kafka.topic}")
	private String topic;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;


	@SneakyThrows
	public void publish(Phrase phrase) {
		kafkaTemplate.send(topic, objectMapper.writeValueAsString(phrase));
	}

}
