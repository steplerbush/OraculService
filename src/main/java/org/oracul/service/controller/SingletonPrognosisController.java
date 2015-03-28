package org.oracul.service.controller;

import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.util.PredictionQueue;
import org.oracul.service.util.PredictionResult;
import org.oracul.service.util.PredictionResultStore;
import org.oracul.service.util.StatusPredictionHolder;
import org.oracul.service.worker.PredictionTask;
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
	private PredictionQueue<PredictionTask> queue;

	@Autowired
	private PredictionExecutor executor;

	@Autowired
	private PredictionResultStore result;

	@Autowired
	private StatusPredictionHolder statusHolder;

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object orderPrediction() throws InterruptedException {
		PredictionTask task = new PredictionTask3D();
		queue.addTask(task);
		statusHolder.putStatus(task.getId(), PredictionStatus.IN_ORDER);
		if (executor.getState() == Thread.State.WAITING) {
			executor.resumeExecutor();
		}
		return "localhost:8080/OraculService/prediction/" + task.getId();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Object getPrediction(@PathVariable("id") Long id) {
		PredictionResult prediction;
		if ((prediction = result.getResult(id)) != null) {
			return prediction.getResult();
		} else {
			return statusHolder.checkStatus(id);
		}
	}
}
