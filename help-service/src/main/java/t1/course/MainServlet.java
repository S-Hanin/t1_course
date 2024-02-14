package t1.course;

import lombok.SneakyThrows;
import t1.course.system.ApplicationContext;
import t1.course.system.web.UrlHandlerRegistry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

	private UrlHandlerRegistry urlHandlerRegistry;

	@SneakyThrows
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		var handler = urlHandlerRegistry.getHttpMethodHandlers().get("GET " + req.getPathInfo());
		handler.invoke(req, resp);
	}

	@SneakyThrows
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		var handler = urlHandlerRegistry.getHttpMethodHandlers().get("POST " + req.getPathInfo());
		handler.invoke(req, resp);

	}

	@Override
	public void init() {
		ApplicationContext context = new ApplicationContext(this);
		context.init();
		urlHandlerRegistry = context.getWheel(UrlHandlerRegistry.class);
	}
}