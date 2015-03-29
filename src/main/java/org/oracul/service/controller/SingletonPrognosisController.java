package org.oracul.service.controller;

import org.oracul.service.builder.PredictionBuilder2D;
import org.oracul.service.builder.PredictionBuilder3D;
import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.util.PredictionQueue;
import org.oracul.service.util.PredictionResultStore;
import org.oracul.service.util.PredictionStatusHolder;
import org.oracul.service.worker.PredictionTask;
import org.oracul.service.worker.PredictionTask2D;
import org.oracul.service.worker.PredictionTask3D;
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
	private PredictionQueue queue;

	@Autowired
	private PredictionExecutor executor;

	@Autowired
	private PredictionResultStore store;

	@Autowired
	private PredictionStatusHolder statusHolder;

	@Autowired
	private PredictionBuilder2D builder2D;
	
	@Autowired
	private PredictionBuilder3D builder3D;

	@RequestMapping(value = "/order2d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object order2dPrediction() throws InterruptedException {
		PredictionTask task = new PredictionTask2D();
		return orderPrediction(task);
	}

	@RequestMapping(value = "/order3d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object order3dPrediction() throws InterruptedException {
		PredictionTask task = new PredictionTask3D();
		task.setBuilder(builder2D);
		return orderPrediction(task);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Object getPrediction(@PathVariable("id") Long id) {
		Object prediction;
		if ((prediction = store.getResult(id)) != null) {
			return prediction;
		} else {
			return statusHolder.checkStatus(id);
		}
	}

	public Object orderPrediction(PredictionTask task) throws InterruptedException {
		queue.addTask(task);
		statusHolder.putStatus(task.getId(), PredictionStatus.IN_ORDER);
		if (executor.getState() == Thread.State.WAITING) {
			executor.resumeExecutor();
		}
		return "localhost:8080/OraculService/prediction/" + task.getId();
	}
}
