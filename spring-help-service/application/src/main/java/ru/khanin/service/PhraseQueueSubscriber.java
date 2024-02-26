package ru.khanin.service;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.khanin.dto.Phrase;
import ru.lib.khanin.MessageQueue;
import ru.lib.khanin.Subscriber;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhraseQueueSubscriber {

	private final MessageQueue<Phrase> messageQueue;
	private final HelpSupportService helpSupportService;

	@Subscriber
	public void run() {
		while (true) {
			log.debug("Trying to get a phrase from queue");
			var phrase = messageQueue.poll();
			Optional.ofNullable(phrase)
				.ifPresentOrElse(
					helpSupportService::addPhrase,
					() -> Try.run(() -> Thread.sleep(1000))
						.getOrElseThrow(() -> new RuntimeException("Subscription interrupted")));
		}
	}
}
