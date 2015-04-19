package org.oracul.service.controller;

import org.apache.log4j.Logger;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.executor.PredictionExecutorPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExecutorStartupController {

	private static final Logger LOGGER = Logger.getLogger(ExecutorStartupController.class);

	@Autowired
	private PredictionExecutorPool executorPool;

	@Autowired
	private PredictionExecutor predictionExecutor;

	@RequestMapping("/start")
	@ResponseStatus(HttpStatus.OK)
	public String startService() {
		executorPool.initService();
		LOGGER.debug("\nPool is started\n");
		if (!predictionExecutor.isAlive()) {
			predictionExecutor.start();
			LOGGER.debug("\nExecutor is started\n");

		}
		return "ORACUL IS WORKING";
	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String stopService() {
		executorPool.stopService();
		LOGGER.debug("Pool is stopped");
		predictionExecutor.suspendExecutor();
		LOGGER.debug("Executor is suspended");
		return "ORACUL IS STOPPED";
	}
}
