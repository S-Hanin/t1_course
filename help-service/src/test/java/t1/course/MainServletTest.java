package t1.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import t1.course.service.SupportService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MainServletTest {

	private SupportService supportService;
	private MainServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private StringWriter writer;

	@BeforeEach
	void setUp() throws IOException {
		supportService = mock(SupportService.class);

		servlet = new MainServlet();
		servlet.setSupportService(supportService);

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		writer = new StringWriter();

		when(response.getWriter()).thenReturn(new PrintWriter(writer));
	}

	@Test
	void doGet_returnResponse_whenRequest() throws IOException {
		//given
		when(supportService.getRandomPhrase()).thenReturn("test advice");

		//when
		servlet.doGet(request, response);

		//then
		assertThat(writer.toString()).isNotEmpty();
	}

	@Test
	void doPost_returnOk_whenRequest() throws IOException {
		//given
		var reader = mock(BufferedReader.class);
		when(reader.lines()).thenReturn(Stream.of("test advice"));
		when(request.getReader()).thenReturn(reader);

		//when
		servlet.doPost(request, response);

		//then
		verify(supportService).addPhrase(anyString());
	}


}