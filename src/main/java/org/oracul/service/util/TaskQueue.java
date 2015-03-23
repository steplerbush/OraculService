package org.oracul.service.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.oracul.service.dto.PredictionTask;
import org.oracul.service.exception.QueueOverflowException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskQueue {

	@Value("${queue.size}")
	private int queueSize;

	@Value("${timeout}")
	private int timeout;

	private LinkedBlockingQueue<PredictionTask> queue;

	@PostConstruct
	public void initQueue() {
		queue = new LinkedBlockingQueue<>(queueSize);
	}

	public void addTask(PredictionTask task) throws InterruptedException {
		if (!queue.offer(task, timeout, TimeUnit.MILLISECONDS)) {
			throw new QueueOverflowException();
		}
	}

	public boolean isEmpty() {
		return queue.size() == 0;
	}

	public PredictionTask getTask() {
		return queue.poll();
	}

}
