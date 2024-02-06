package t1.course.repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySupportRepository implements SupportRepository {

	private final Set<String> storage = ConcurrentHashMap.newKeySet();

	@Override
	public String getRandomPhrase() {
		return storage.stream()
			.skip((int) (storage.size() * Math.random()))
			.findFirst()
			.orElse("There's no advices yet");
	}

	@Override
	public void addPhrase(String phrase) {
		storage.add(phrase);
	}
}
