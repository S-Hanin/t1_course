package ru.khanin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.khanin.dto.Phrase;
import ru.khanin.service.HelpSupportService;
import ru.khanin.service.PhraseQueuePublisher;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HelpController {

	private final PhraseQueuePublisher phraseQueuePublisher;
	private final HelpSupportService helpSupportService;

	@GetMapping(value = "/support", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Phrase> getSupport() {
		return ResponseEntity.ok(helpSupportService.getRandomPhrase());
	}

	@PostMapping(value = "/support", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addSupportPhrase(@RequestBody Phrase phrase) {
		phraseQueuePublisher.publish(phrase);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
