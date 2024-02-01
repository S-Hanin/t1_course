package t1.course;

import t1.course.repository.InMemorySupportRepository;
import t1.course.service.SupportService;
import t1.course.service.SupportServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class MainServlet extends HttpServlet {

	private SupportService supportService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println(supportService.getRandomPhrase());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		var phrase = req.getReader().lines()
			.collect(Collectors.joining());
		supportService.addPhrase(phrase);
		resp.setStatus(200);
	}

	@Override
	public void init() {
		var supportRepository = new InMemorySupportRepository();
		supportService = new SupportServiceImpl(supportRepository);
	}

	void setSupportService(SupportService supportService) {
		this.supportService = supportService;
	}
}