import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.khanin.App;
import ru.khanin.dto.Phrase;
import ru.khanin.repository.InMemorySupportRepository;
import ru.khanin.service.PhrasePublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class AppTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@SpyBean
	private InMemorySupportRepository repository;
	@SpyBean
	private PhrasePublisher publisher;

	@Test
	void getSupportPhrase_shouldReturnDefaultPhrase_whenNoPhrases() throws Exception {
		var defaultPhrase = "There's no phrases yet";

		mockMvc.perform(get("/support"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.text").value(defaultPhrase));
	}

	@Test
	@DirtiesContext
	void addSupportPhrase_shouldSavePhraseToStorage() throws Exception {
		var phrase = new Phrase("Looking amazing!");

		mockMvc.perform(post("/support")
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsBytes(phrase)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().string(""));

		verify(publisher).publish(any());
		verify(repository, timeout(2000)).savePhrase(any());
	}
}
