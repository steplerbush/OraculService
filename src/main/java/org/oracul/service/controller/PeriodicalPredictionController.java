package org.oracul.service.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.oracul.service.dto.PeriodicalPrediction;
import org.oracul.service.task.PredictionTaskCreator;
import org.oracul.service.util.IntegrationFacade;
import org.oracul.service.util.exception.QueueOverflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prediction")
public class PeriodicalPredictionController {
	private static final Logger LOGGER = Logger.getLogger(PeriodicalPredictionController.class);

	@Value("${oracul.periodical.maxPredictionSeconds}")
	private int maxPredictionSeconds;

	@Value("${oracul.periodical.maxPredictionCount}")
	private int maxPredictionCount;

	@Autowired
	private PredictionTaskCreator predictionTaskCreator;

	@Autowired
	private IntegrationFacade facade;

	@RequestMapping(value = "/order/periodical/{predictionCount}/{predictionTimeStep}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object orderPeriodicalPrediction(HttpServletRequest request, @PathVariable Integer predictionTimeStep,
			@PathVariable Integer predictionCount) throws InterruptedException {
		if (predictionCount * predictionTimeStep >= maxPredictionSeconds) {
			return "maximum supported prediction is " + maxPredictionSeconds / 3600 + " hours";
		}

		if (facade.getQueue().size() >= facade.getQueue().getMaxSize()) {
			LOGGER.debug("Queue is overloaded. Task is rejected.");
			throw new QueueOverflowException();
		}
		Long taskID = predictionTaskCreator.createPrediction(PredictionTaskCreator.PredictionType.PERIODICAL_TASK_2D,
				predictionCount.toString(), predictionTimeStep.toString());
		return request.getRequestURL().substring(0, request.getRequestURL().indexOf("order")) + "periodical/" + taskID;
	}

	@RequestMapping(value = "/periodical/{id}", method = RequestMethod.GET)
	public Object getPrediction2D(@PathVariable("id") Long id) {
		PeriodicalPrediction prediction = facade.getPeriodicalPredictionRepository().findById(id);
		if (prediction != null && prediction.getPredictions() != null) {
			return prediction;
		} else {
			return facade.getStatusHolder().checkStatus(id);
		}
	}

}
