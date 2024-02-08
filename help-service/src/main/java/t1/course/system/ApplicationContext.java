package t1.course.system;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import t1.course.system.core.Wheel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationContext {

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private final Map<Class<?>, Object> context = new HashMap<>();
	private final String packageName;

	public ApplicationContext(Object application) {
		this.packageName = application.getClass().getPackageName();
	}

	public void init() {
		var wheelTypes = findWheelTypes();
		populateContext(wheelTypes);
		log.log(Level.SEVERE, context.toString());
//		context.forEach((k, v) -> log.debug(k.getSimpleName()));
	}

	private Set<Class<?>> findWheelTypes() {
		var reflections = new Reflections(packageName);
		var wheels = reflections.getTypesAnnotatedWith(Wheel.class);
		return wheels;
	}

	private void populateContext(Set<Class<?>> wheelTypes) {
		while (!context.keySet().containsAll(wheelTypes)) {
			wheelTypes.stream()
				.sorted(Comparator.comparingInt(it -> it.getDeclaredConstructors()[0].getParameterTypes().length))
				.forEach(it -> {
					if (context.containsKey(it)) {
						return;
					}

					Optional.ofNullable(createWheel(it))
						.ifPresent(wheel -> {
							context.put(it, wheel);
							Arrays.stream(it.getInterfaces())
								.forEach(_interface -> context.put(_interface, wheel));
						});
				});
		}
	}

	@SneakyThrows
	private Object createWheel(Class<?> it) {
		var constructors = it.getDeclaredConstructors();
		var parameterTypes = constructors[0].getParameterTypes();
		var objects = Arrays.stream(parameterTypes)
			.map(param -> context.getOrDefault(param, null))
			.toList();
		if (objects.contains(null)) return null;
		return constructors[0].newInstance(objects.toArray());
	}

	public <T> T getWheel(Class<T> wheelType) {
		return wheelType.cast(context.get(wheelType));
	}
}
