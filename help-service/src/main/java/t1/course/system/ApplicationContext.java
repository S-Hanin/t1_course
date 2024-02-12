package t1.course.system;

import io.vavr.control.Try;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import t1.course.system.core.Wheel;
import t1.course.system.core.WheelPostProcessor;
import t1.course.system.core.WheelsCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ApplicationContext {

	private final Map<Class<?>, Object> context = new HashMap<>();
	private final String packageName;

	public ApplicationContext(Object application) {
		this.packageName = application.getClass().getPackageName();
	}

	public void init() {
		processWheelTypes();
		log.debug(context.toString());
	}

	private void processWheelTypes() {
		var reflections = new Reflections(packageName);
		runWheelCreators(reflections);
		runWheelPostProcessors(reflections);
	}

	private void runWheelCreators(Reflections reflections) {
		var processorTypes = reflections.getSubTypesOf(WheelsCreator.class);
		var wheelProcessors = processorTypes.stream().map(ApplicationContext::getNewCreatorInstance).toList();
		var wheelTypes = reflections.getTypesAnnotatedWith(Wheel.class);

		wheelProcessors.forEach(processor -> processor.process(wheelTypes, this));
	}

	private void runWheelPostProcessors(Reflections reflections) {
		var processorTypes = reflections.getSubTypesOf(WheelPostProcessor.class);
		var wheelProcessors = processorTypes.stream().map(ApplicationContext::getNewPostProcessorInstance).toList();

		wheelProcessors.forEach(it -> context.put(it.getClass(), it));
		context.entrySet().stream()
			.distinct()
			.forEach(entry -> wheelProcessors
				.forEach(processor -> processor.process(entry.getKey(), entry.getValue())));
	}

	@SneakyThrows
	private static WheelsCreator getNewCreatorInstance(Class<? extends WheelsCreator> it) {
		return it.getDeclaredConstructor().newInstance();
	}

	@SneakyThrows
	private static WheelPostProcessor getNewPostProcessorInstance(Class<? extends WheelPostProcessor> it) {
		return it.getDeclaredConstructor().newInstance();
	}

	public <T> void addWheel(Class<T> wheelType, Object wheel) {
		context.put(wheelType, wheel);
	}

	@SuppressWarnings("unchecked")
	public <T> T getWheel(Class<T> wheelType) {
		if (context.containsKey(wheelType)) {
			return (T) context.get(wheelType);
		}

		var key = context.keySet().stream()
			.filter(it -> Arrays.asList(it.getInterfaces()).contains(wheelType))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("Wheel not found in context: " + wheelType.getName()));
		return (T) context.get(key);
	}

	public <T> T getWheelOrNull(Class<T> wheelType) {
		return Try.of(() -> getWheel(wheelType))
			.getOrNull();
	}

	public boolean containsAll(Set<Class<?>> wheelTypes) {
		return context.keySet().containsAll(wheelTypes);
	}
}
