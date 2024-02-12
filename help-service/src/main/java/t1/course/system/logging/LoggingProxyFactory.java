package t1.course.system.logging;

import t1.course.system.core.WheelProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LoggingProxyFactory implements WheelProxyFactory {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T createProxy(Class<T> wheelType, Object wheel) {
		if (Arrays.stream(wheel.getClass().getMethods())
			.anyMatch(method -> method.isAnnotationPresent(Log.class))) {
			return (T) Proxy.newProxyInstance(
				wheelType.getClassLoader(),
				wheelType.getInterfaces(),
				new LogInvocationHandler(wheel)
			);
		}
		return (T) wheel;
	}


	public static class LogInvocationHandler implements InvocationHandler {

		private final Object origin;

		public LogInvocationHandler(Object origin) {
			this.origin = origin;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			var originMethod = origin.getClass()
				.getMethod(method.getName(), method.getParameterTypes());

			if (originMethod.isAnnotationPresent(Log.class)) {
				System.out.println("Called method: " + method.getName());
			}
			return method.invoke(origin, args);
		}
	}
}
