package org.oracul.service.controller;

import org.apache.log4j.Logger;
import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.dto.PredictionTask;
import org.oracul.service.util.PredictionResult;
import org.oracul.service.util.PredictionResultHolder;
import org.oracul.service.util.PredictionWorker;
import org.oracul.service.util.StatusTaskHolder;
import org.oracul.service.util.TaskQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingletonPrognosisController {

	private static final Logger logger = Logger
			.getLogger(SingletonPrognosisController.class);

	@Autowired
	private TaskQueue queue;

	@Autowired
	private PredictionWorker worker;

	@Autowired
	private PredictionResultHolder result;

	@Autowired
	private StatusTaskHolder statusHolder;

	@RequestMapping(value = "/orderPrediction", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object orderPrediction() throws InterruptedException {
		PredictionTask task = new PredictionTask();
		queue.addTask(task);
		statusHolder.putStatus(task.getId(), PredictionStatus.IN_ORDER);
		return "localhost:8080/OraculService/prediction/" + task.getId();
	}

	@RequestMapping(value = "/prediction/{id}", method = RequestMethod.GET)
	public Object getPrediction(@PathVariable("id") Long id) {
		PredictionResult prediction;
		if ((prediction = result.getResult(id)) != null) {
			return prediction.getResult();
		} else {
			return statusHolder.checkStatus(id);
		}
	}
}
