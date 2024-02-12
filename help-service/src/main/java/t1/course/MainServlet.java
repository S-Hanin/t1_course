package t1.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import t1.course.system.ApplicationContext;
import t1.course.system.web.UrlHandlerRegistry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class MainServlet extends HttpServlet {

	private UrlHandlerRegistry urlHandlerRegistry;
	private ObjectMapper objectMapper;

	@SneakyThrows
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		var handler = urlHandlerRegistry.getHttpMethodHandlers().get("GET " + req.getPathInfo());
		var controller = handler._1();
		var method = handler._2();

		resp.setContentType("application/json");
		var result = method.invoke(controller);
		resp.getWriter().println(objectMapper.writeValueAsString(result));
	}

	@SneakyThrows
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		var handler = urlHandlerRegistry.getHttpMethodHandlers().get("POST " + req.getPathInfo());
		var controller = handler._1();
		var method = handler._2();
		var requestBody = method.getParameters()[0];

		var parser = objectMapper.createParser(req.getReader().lines().collect(Collectors.joining()));
		Object body = parser.readValueAs(requestBody.getType());

		resp.setContentType("application/json");
		var result = method.invoke(controller, body);
		resp.getWriter().println(objectMapper.writeValueAsString(result));
	}

	@Override
	public void init() {
		ApplicationContext context = new ApplicationContext(this);
		context.init();
		urlHandlerRegistry = context.getWheel(UrlHandlerRegistry.class);
		objectMapper = new ObjectMapper();
	}
}