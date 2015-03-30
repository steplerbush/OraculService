package org.oracul.service.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.oracul.service.util.exception.QueueOverflowException;
import org.oracul.service.task.PredictionTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionQueue {

	private static final Logger LOGGER = Logger.getLogger(PredictionQueue.class);

	@Value("${queue.size}")
	private Integer queueSize;

	@Value("${timeout}")
	private Integer timeout;

	private LinkedBlockingQueue<PredictionTask> queue;

	@PostConstruct
	private void initQueue() {
		queue = new LinkedBlockingQueue<>(queueSize);
		LOGGER.debug("Queue is created with size: " + queueSize);
	}

	public void addTask(PredictionTask task) throws InterruptedException {
		if (!queue.offer(task, timeout, TimeUnit.MILLISECONDS)) {
			LOGGER.debug("Queue is overloaded. Task is rejected.");
			throw new QueueOverflowException();
		}
		LOGGER.debug("Task is added with ID=" + task.getId());
		LOGGER.debug("Queue size: " + queue.size());
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public PredictionTask getTask() {
		PredictionTask task = queue.poll();
		LOGGER.debug("Queue size: " + queue.size());
		return task;
	}

	public Integer getNextLoad() {
		if (queue.peek() == null) {
			LOGGER.debug("getNextLoad() - TASK is null");
			return 0;
		}
		LOGGER.debug("TASK id = " + queue.peek().getId());
		return queue.peek().getCores();
	}

}
