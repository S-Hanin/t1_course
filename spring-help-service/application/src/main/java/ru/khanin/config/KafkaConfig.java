package ru.khanin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.khanin.service.HelpSupportService;
import ru.khanin.service.PhraseConsumer;
import ru.khanin.service.PhrasePublisher;

import java.time.Instant;

@Configuration
@ConditionalOnProperty(prefix = "external.kafka", value = "enabled", havingValue = "true")
public class KafkaConfig {

	@Bean
	@Primary
	@ConfigurationProperties("external.kafka.setup")
	public KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}

	@Bean
	public ProducerFactory<String, String> producerFactory(KafkaProperties properties) {
		return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties(null));
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory(KafkaProperties properties) {
		var consumerProperties = properties.buildConsumerProperties(null);
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public KafkaListenerContainerFactory<?> phraseListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
		var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
		factory.setConsumerFactory(consumerFactory);
		return factory;
	}

	@Bean
	public PhrasePublisher phrasePublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		return new PhrasePublisher(kafkaTemplate, objectMapper);
	}

	@Bean
	public PhraseConsumer phraseConsumer(HelpSupportService helpSupportService, ObjectMapper objectMapper) {
		return new PhraseConsumer(helpSupportService, objectMapper);
	}

}
