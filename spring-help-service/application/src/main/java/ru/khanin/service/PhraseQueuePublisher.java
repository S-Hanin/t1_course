package ru.khanin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.khanin.dto.Phrase;
import ru.lib.khanin.MessageQueue;

@Component
@RequiredArgsConstructor
public class PhraseQueuePublisher {

	private final MessageQueue<Phrase> messageQueue;

	public void publish(Phrase phrase) {
		messageQueue.publish(phrase);
	}

}
