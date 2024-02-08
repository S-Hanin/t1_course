package t1.course.repository;

import t1.course.system.core.Wheel;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Wheel
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
