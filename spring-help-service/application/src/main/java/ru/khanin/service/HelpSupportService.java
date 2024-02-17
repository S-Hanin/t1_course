package ru.khanin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.khanin.dto.Phrase;
import ru.khanin.repository.InMemorySupportRepository;

@Service
@RequiredArgsConstructor
public class HelpSupportService {

	private final InMemorySupportRepository repository;

	public Phrase getRandomPhrase() {
		return repository.getRandomPhrase();
	}

	public void addPhrase(Phrase phrase) {
		repository.savePhrase(phrase);
	}
}
