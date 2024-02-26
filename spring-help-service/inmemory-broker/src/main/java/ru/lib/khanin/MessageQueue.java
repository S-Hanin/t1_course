package ru.lib.khanin;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageQueue<T> {

	private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

	public void publish(T message) {
		queue.offer(message);
	}

	public T poll() {
		return queue.poll();
	}
}
