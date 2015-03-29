package org.oracul.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.oracul.service.worker.PredictionTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PredictionExecutorPool {

	private ExecutorService service;

	@Value("${service.pool.size}")
	private Integer poolSize;

	public void initService() {
		if (service == null || service.isShutdown() || service.isTerminated()) {
			service = Executors.newFixedThreadPool(poolSize);
		}
	}

	public void executePrediction(PredictionTask task) {
		service.submit(task);
	}

	public void stopService() {
		if (service != null || !service.isShutdown() || !service.isTerminated()) {
			service.shutdown();
		}
	}

	public Integer getPoolSize() {
		return poolSize;
	}
}
