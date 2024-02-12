package t1.course.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import t1.course.service.SupportService;
import t1.course.system.core.Wheel;
import t1.course.system.web.HttpMethodHandler;

@Wheel
@RequiredArgsConstructor
public class SupportController {

	private final SupportService supportService;

	@HttpMethodHandler(method="GET", value = "/v1/support")
	public Message getRandomPhrase() {
		return new Message(supportService.getRandomPhrase());
	}

	@HttpMethodHandler(method="POST", value = "/v1/support")
	public Message addPhrase(Message phrase) {
		supportService.addPhrase(phrase.getText());
		return phrase;
	}


	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Message {
		private String text;
	}
}
