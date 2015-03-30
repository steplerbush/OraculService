package org.oracul.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.oracul.service.task.PredictionTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PredictionExecutorPool {

	private static final Logger LOGGER = Logger.getLogger(PredictionExecutorPool.class);
	private ExecutorService service;

	@Value("${service.pool.size}")
	private Integer poolSize;

	public void initService() {
		if (service == null || service.isShutdown() || service.isTerminated()) {
			LOGGER.debug("PredictionExecutorPool: initializing");
			service = Executors.newFixedThreadPool(poolSize);
		}
	}

	public void executePrediction(PredictionTask task) {
		LOGGER.debug("PredictionExecutorPool: submitting task #" + task.getId());
		service.execute(task);
	}

	public void stopService() {
		if (service != null || !service.isShutdown() || !service.isTerminated()) {
			LOGGER.debug("PredictionExecutorPool: shutting down");
			service.shutdown();
		}
	}

	public Integer getPoolSize() {
		return poolSize;
	}
}
