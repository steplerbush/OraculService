package org.oracul.service.controller;

import org.apache.log4j.Logger;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.executor.PredictionExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OraculStartupController {

	@Autowired
	private PredictionExecutorService executorService;

	@Autowired
	private PredictionExecutor predictionExecutor;

	@ResponseStatus(HttpStatus.OK)
	public String startService() {
		executorService.startService();
		predictionExecutor.start();
		predictionExecutor.suspendExecutor();
		return "ORACUL IS WORKING";
	}

	@RequestMapping(value = "stop", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String stopService() {
		executorService.stopService();
		predictionExecutor.suspendExecutor();
		return "ORACUL IS STOPPED";
	}
}
