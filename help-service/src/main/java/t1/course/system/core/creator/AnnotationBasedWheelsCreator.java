package t1.course.system.core.creator;

import lombok.SneakyThrows;
import t1.course.system.ApplicationContext;
import t1.course.system.core.WheelProxyFactory;
import t1.course.system.core.WheelsCreator;
import t1.course.system.logging.LoggingProxyFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AnnotationBasedWheelsCreator implements WheelsCreator {

	private final List<WheelProxyFactory> proxyFactories = getWheelProxyFactories();

	@Override
	public void process(Set<Class<?>> wheelTypes, ApplicationContext context) {
		var definitions = createWheelDefinitions(wheelTypes);
		while (!context.containsAll(wheelTypes)) {
			definitions.stream()
				.sorted(Comparator.comparingInt(it -> it.getConstructorParameters().length))
				.forEach(it -> {
					if (context.getWheelOrNull(it.getWheelClass()) != null) {
						return;
					}

					Optional.ofNullable(createWheel(it, context))
						.ifPresent(wheel -> context.addWheel(it.getWheelClass(), wheel));
				});
		}
	}

	private List<WheelProxyFactory> getWheelProxyFactories() {
		return List.of(new LoggingProxyFactory());
	}

	private List<WheelDefinition> createWheelDefinitions(Set<Class<?>> wheelTypes) {
		return wheelTypes.stream()
			.map(it -> {
				var definition = new WheelDefinition();
				definition.setWheelClass(it);
				definition.setWheelConstructor(it.getDeclaredConstructors()[0]);
				definition.setConstructorParameters(it.getDeclaredConstructors()[0].getParameterTypes());
				definition.setWheelInterfaces(it.getInterfaces());
				return definition;
			}).toList();
	}

	@SneakyThrows
	private Object createWheel(WheelDefinition it, ApplicationContext context) {
		var objects = Arrays.stream(it.getConstructorParameters())
			.map(context::getWheelOrNull)
			.toList();
		if (objects.contains(null)) return null;
		var wheel = it.getWheelConstructor().newInstance(objects.toArray());
		for (var proxyFactory : proxyFactories) {
			wheel = proxyFactory.createProxy(wheel.getClass(), wheel);
		}
		return wheel;
	}
}
