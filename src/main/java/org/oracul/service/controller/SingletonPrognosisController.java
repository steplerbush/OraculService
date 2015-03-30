package org.oracul.service.controller;

import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.util.PredictionsResultsHolder;
import org.oracul.service.util.PredictionsStatusesHolder;
import org.oracul.service.task.PredictionTaskCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prediction")
public class SingletonPrognosisController {

	@Autowired
	private PredictionTaskCreator predictionTaskCreator;

	@Autowired
	private PredictionExecutor executor;

	@Autowired
	private PredictionsResultsHolder store;

	@Autowired
	private PredictionsStatusesHolder statusHolder;

	@RequestMapping(value = "/order2d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object order2dPrediction() throws InterruptedException {
		Long taskID = predictionTaskCreator.createPrediction(PredictionTaskCreator.PredictionType.TASK_2D,
				new String[]{"some params for 2d"});
		return "localhost:8080/OraculService/prediction/" + taskID;
	}

	@RequestMapping(value = "/order3d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object order3dPrediction() throws InterruptedException {
		Long taskID = predictionTaskCreator.createPrediction(PredictionTaskCreator.PredictionType.TASK_3D,
				new String[]{"some params for 3d"});
		return "localhost:8080/OraculService/prediction/" + taskID;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Object getPrediction(@PathVariable("id") Long id) {
		Object prediction = store.getResult(id);
		if (prediction != null) {
			return prediction;
		} else {
			return statusHolder.checkStatus(id);
		}
	}
}
