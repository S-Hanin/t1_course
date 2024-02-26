package ru.lib.khanin;

import io.vavr.control.Try;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SubscriberRunnerBeanPostProcessor implements BeanPostProcessor {

	private final Map<String, Object> subscribers = new HashMap<>();
	private final ExecutorService executorService = Executors.newFixedThreadPool(4);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (Arrays.stream(bean.getClass().getMethods()).anyMatch(it -> it.isAnnotationPresent(Subscriber.class))) {
			subscribers.put(beanName, bean);
		}

		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		var object = subscribers.get(beanName);
		if (Objects.isNull(object)) return bean;

		Arrays.stream(object.getClass().getMethods())
			.filter(it -> it.isAnnotationPresent(Subscriber.class))
			.forEach(it ->
				executorService.execute(
					() -> Try.run(() -> it.invoke(bean))
				)
			);

		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
}
