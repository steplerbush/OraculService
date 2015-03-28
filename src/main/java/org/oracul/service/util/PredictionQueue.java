package org.oracul.service.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.oracul.service.exception.QueueOverflowException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionQueue<T> {

	@Value("${queue.size}")
	private int queueSize;

	@Value("${timeout}")
	private int timeout;

	private LinkedBlockingQueue<T> queue;

	@PostConstruct
	public void initQueue() {
		queue = new LinkedBlockingQueue<T>(queueSize);
	}

	public void addTask(T task) throws InterruptedException {
		if (!queue.offer(task, timeout, TimeUnit.MILLISECONDS)) {
			throw new QueueOverflowException();
		}
	}

	public boolean isEmpty() {
		return queue.size() == 0;
	}

	public T getTask() {
		return queue.poll();
	}

}
