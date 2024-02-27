package ru.khanin.repository;

import org.springframework.stereotype.Repository;
import ru.khanin.dto.Phrase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySupportRepository {

	private final Map<UUID, Phrase> storage = new ConcurrentHashMap<>();

	public Phrase getRandomPhrase() {
		if (storage.isEmpty()) {
			return new Phrase("There's no phrases yet");
		}

		var phrases = new ArrayList<>(storage.values());
		Collections.shuffle(phrases);
		return phrases.get(0);
	}

	public void savePhrase(Phrase phrase) {
		if (!storage.containsValue(phrase)) {
			storage.put(UUID.randomUUID(), phrase);
		}
	}
}
