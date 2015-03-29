package org.oracul.service.executor;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.util.PredictionQueue;
import org.oracul.service.util.PredictionStatusHolder;
import org.oracul.service.worker.PredictionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionExecutor extends Thread {

	private static final Logger LOGGER = Logger.getLogger(PredictionExecutor.class);

	@Autowired
	private PredictionExecutorPool predictionPool;

	@Autowired
	private PredictionQueue predictionQueue;

	@Autowired
	private PredictionStatusHolder statusHolder;

	private AtomicInteger currentLoad = new AtomicInteger();

	@Value("${service.pool.size}")
	private Integer maxLoad;

	private boolean suspendFlag;

	public PredictionExecutor() {
		setDaemon(true);
	}

	public void load(int load) {
		currentLoad.addAndGet(load);
	}

	public void unload(int unload) {
		currentLoad.addAndGet(-unload);
	}

	public synchronized void suspendExecutor() {
		suspendFlag = true;
	}

	public synchronized void resumeExecutor() {
		suspendFlag = false;
		LOGGER.debug("\nExcutor is resuming.\n");
		notify();
	}

	public void run() {
		while (!isInterrupted()) {
			checkIsEmptyQueue();
			waitIfSuspended();
			if ((currentLoad.get() + predictionQueue.getNextLoad()) <= maxLoad) {
				LOGGER.debug("\nMax system load: " + maxLoad + "\n");
				LOGGER.debug("\nCurrent system load: " + currentLoad + "\n");
				PredictionTask task = predictionQueue.getTask();
				task.setExecutor(this);
				LOGGER.debug("\nTask#" + task.getId() + " load: " + task.getLoad() + "\n");
				statusHolder.putStatus(task.getId(), PredictionStatus.PENDING);
				load(task.getLoad());
				predictionPool.executePrediction(task);
			} else {
				LOGGER.debug("WAITING. SYSTEM IS OVERLOAD.");
			}
		}
	}

	private void checkIsEmptyQueue() {
		if (predictionQueue.isEmpty()) {
			LOGGER.debug("\nChecking queue. Queue is empty.\n");
			suspendExecutor();
		}

	}

	private void waitIfSuspended() {
		synchronized (this) {
			while (suspendFlag) {
				try {
					LOGGER.debug("\nExecutor is waiting for tasks\n");
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
