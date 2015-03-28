package org.oracul.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PredictionExecutorService {

	private ExecutorService service;
	private boolean started;

	@Value("${service.load}")
	private Integer load;

	public void startService() {
		if (service == null || service.isShutdown() || service.isTerminated()) {
			initService();
		}
	}

	public void executePrediction(Runnable task) {
		service.submit(task);
	}

	private void initService() {
		service = Executors.newFixedThreadPool(load);
		started = true;
	}

	public void stopService() {
		if (service != null || !service.isShutdown() || !service.isTerminated()) {
			service.shutdown();
			started = false;
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Integer getLoad() {
		return load;
	}
}
