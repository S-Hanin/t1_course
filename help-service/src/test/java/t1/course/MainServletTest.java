package t1.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MainServletTest {

	private MainServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private StringWriter writer;

	@BeforeEach
	void setUp() throws IOException {

		servlet = new MainServlet();
		servlet.init();

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		writer = new StringWriter();

		when(response.getWriter()).thenReturn(new PrintWriter(writer));
	}

	@Test
	void doGet_returnResponse_whenRequest() {
		//given
		var body = "{\"text\":\"There's no advices yet\"}";
		when(request.getPathInfo()).thenReturn("/v1/support");
		when(request.getMethod()).thenReturn("GET");

		//when
		servlet.doGet(request, response);

		//then
		assertThat(writer.toString()).isEqualToIgnoringNewLines(body);
	}

	@Test
	void doPost_returnOk_whenRequest() throws IOException {
		//given
		var body = "{\"text\":\"test advice\"}";
		when(request.getPathInfo()).thenReturn("/v1/support");
		when(request.getMethod()).thenReturn("POST");

		var reader = mock(BufferedReader.class);
		when(reader.lines()).thenReturn(Stream.of(body));
		when(request.getReader()).thenReturn(reader);

		//when
		servlet.doPost(request, response);

		//then
		assertThat(writer.toString()).isEqualToIgnoringNewLines(body);
	}


}