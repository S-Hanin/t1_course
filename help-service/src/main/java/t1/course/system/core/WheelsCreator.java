package t1.course.system.core;

import t1.course.system.ApplicationContext;

import java.util.Set;

public interface WheelsCreator {
	void process(Set<Class<?>> wheelTypes, ApplicationContext context);
}
