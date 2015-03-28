package org.oracul.service.executor;

import java.util.concurrent.atomic.AtomicInteger;

import org.oracul.service.util.PredictionQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PredictionExecutor extends Thread {

	@Autowired
	private PredictionExecutorService service;

	@Autowired
	private PredictionQueue<Runnable> queueTask;

	private AtomicInteger currentLoad = new AtomicInteger();
	private int totalLoad;
	private boolean suspendFlag;

	public PredictionExecutor() {
		setDaemon(true);
		totalLoad = service.getLoad();
		suspendFlag = false;
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
		notify();
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				if (!queueTask.isEmpty()) {
					suspendExecutor();
				}
				synchronized (this) {
					while (suspendFlag) {
						wait();
					}
				}
				if (service.isStarted() && (currentLoad.get() < totalLoad)) {
					service.executePrediction(queueTask.getTask());
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
