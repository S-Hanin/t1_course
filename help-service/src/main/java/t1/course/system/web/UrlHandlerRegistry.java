package t1.course.system.web;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import t1.course.system.core.WheelPostProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class UrlHandlerRegistry implements WheelPostProcessor {

	private Map<String, Tuple2<Object, Method>> httpMethodHandlers = new HashMap<>();

	@Override
	public void process(Class<?> wheelType, Object wheel) {

		Arrays.stream(wheel.getClass().getMethods())
			.filter(method -> method.isAnnotationPresent(HttpMethodHandler.class))
			.forEach(method -> {
				var annotation = method.getAnnotation(HttpMethodHandler.class);
				log.debug("Registering: {} {}", annotation.method(), annotation.value());
				httpMethodHandlers.put(
					String.format("%s %s", annotation.method(), annotation.value()),
					Tuple.of(wheel, method));

			});
	}

}
