package t1.course.system.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import t1.course.system.core.WheelPostProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@Data
public class UrlHandlerRegistry implements WheelPostProcessor {

	private Map<String, MethodInvocationHandler> httpMethodHandlers = new HashMap<>();

	@Override
	public void process(Class<?> wheelType, Object wheel) {
		Arrays.stream(wheel.getClass().getMethods())
			.filter(method -> method.isAnnotationPresent(HttpMethodHandler.class))
			.forEach(method -> {
				var annotation = method.getAnnotation(HttpMethodHandler.class);
				log.debug("Registering: {} {}", annotation.method(), annotation.value());
				var proxy = new MethodInvocationHandler(wheel, method);
				httpMethodHandlers.put(
					String.format("%s %s", annotation.method(), annotation.value()),
					proxy);

			});

	}

	@RequiredArgsConstructor
	public static class MethodInvocationHandler {

		private final Object origin;
		private final Method method;
		private final ObjectMapper objectMapper = new ObjectMapper();
		private final Map<String, BiFunction<Method, HttpServletRequest, Object>> handlers = Map.of(
			"GET", this::handleGet,
			"POST", this::handlePost
		);

		public Object invoke(Object... args) throws Throwable {

			var req = (HttpServletRequest) args[0];
			var res = (HttpServletResponse) args[1];

			var result = handlers.get(req.getMethod()).apply(method, req);

			res.setContentType("application/json");
			res.getWriter().println(objectMapper.writeValueAsString(result));
			return result;
		}

		@SneakyThrows
		private Object handleGet(Method method, HttpServletRequest req) {
			return method.invoke(origin);
		}

		@SneakyThrows
		private Object handlePost(Method method, HttpServletRequest req) {
			var requestBody = method.getParameters()[0];
			var parser = objectMapper.createParser(req.getReader().lines().collect(Collectors.joining()));
			Object body = parser.readValueAs(requestBody.getType());
			return method.invoke(origin, body);
		}
	}


}
