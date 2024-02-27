package ru.khanin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import ru.khanin.dto.Phrase;

@Slf4j
@RequiredArgsConstructor
public class PhraseConsumer {

	private final HelpSupportService helpSupportService;
	private final ObjectMapper objectMapper;

	@SneakyThrows
	@KafkaListener(
		containerFactory = "phraseListenerContainerFactory",
		topicPartitions = {@TopicPartition(
			topic = "${external.kafka.topic}",
			partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))}
	)
	public void phraseConsumer(String phrase) {
		helpSupportService.addPhrase(objectMapper.readValue(phrase, Phrase.class));
	}
}
