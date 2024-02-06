package t1.course.service;

import t1.course.repository.SupportRepository;

public class SupportServiceImpl implements SupportService {

	private final SupportRepository supportRepository;

	public SupportServiceImpl(SupportRepository supportRepository) {
		this.supportRepository = supportRepository;
	}

	@Override
	public String getRandomPhrase() {
		return supportRepository.getRandomPhrase();
	}

	@Override
	public void addPhrase(String phrase) {
		supportRepository.addPhrase(phrase);
	}
}
