package t1.course.system.core;

public interface WheelProxyFactory {

	<T> T createProxy(Class<T> wheelType, Object wheel);
}
