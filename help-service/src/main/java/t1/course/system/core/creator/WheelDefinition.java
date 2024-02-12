package t1.course.system.core.creator;

import lombok.Data;

import java.lang.reflect.Constructor;

@Data
public class WheelDefinition {

	private Class<?> wheelClass;
	private Constructor<?> wheelConstructor;
	private Class<?>[] constructorParameters;
	private Class<?>[] wheelInterfaces;
}
