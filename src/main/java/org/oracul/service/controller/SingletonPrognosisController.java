package org.oracul.service.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.dto.Prediction3D;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.service.Prediction2DService;
import org.oracul.service.service.Prediction3DService;
import org.oracul.service.task.PredictionTaskCreator;
import org.oracul.service.util.IntegrationFacade;
import org.oracul.service.util.PredictionsStatusesHolder;
import org.oracul.service.util.exception.QueueOverflowException;
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

	private static final Logger LOGGER = Logger.getLogger(SingletonPrognosisController.class);

	@Autowired
	private PredictionTaskCreator predictionTaskCreator;

	@Autowired
	private PredictionExecutor executor;

	@Autowired
	private Prediction2DService prediction2dRepository;

	@Autowired
	private Prediction3DService prediction3dRepository;

	@Autowired
	private PredictionsStatusesHolder statusHolder;

	@Autowired
	private IntegrationFacade facade;

	@RequestMapping(value = "/order/2d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object orderPrediction2D(HttpServletRequest request) throws InterruptedException {
		if (facade.getQueue().size() >= facade.getQueue().getMaxSize()) {
			LOGGER.debug("Queue is overloaded. Task is rejected.");
			throw new QueueOverflowException();
		}
		Long taskID = predictionTaskCreator.createPrediction(PredictionTaskCreator.PredictionType.TASK_2D,
				new String[] { "some params for 2d" });
		return request.getRequestURL().substring(0, request.getRequestURL().indexOf("order")) + "2d/" + taskID;
	}

	@RequestMapping(value = "/order/3d", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object orderPrediction3D(HttpServletRequest request) throws InterruptedException {
		if (facade.getQueue().size() >= facade.getQueue().getMaxSize() - facade.getProperty().getCore3d()) {
			LOGGER.debug("Queue is overloaded. Task is rejected.");
			throw new QueueOverflowException();
		}
		Long taskID = predictionTaskCreator.createPrediction(PredictionTaskCreator.PredictionType.TASK_3D,
				new String[] { "some params for 3d" });
		return request.getRequestURL().substring(0, request.getRequestURL().indexOf("order")) + "3d/" + taskID;
	}

	@RequestMapping(value = "/2d/{id}", method = RequestMethod.GET)
	public Object getPrediction2D(@PathVariable("id") Long id) {
		Prediction2D prediction = prediction2dRepository.findById(id);
		if (prediction != null && prediction.getU() != null) {
			return prediction;
		} else {
			return statusHolder.checkStatus(id);
		}
	}

	@RequestMapping(value = "/3d/{id}", method = RequestMethod.GET)
	public Object getPrediction3D(@PathVariable("id") Long id) {
		Prediction3D prediction = prediction3dRepository.findById(id);
		if (prediction != null && prediction.getLevels() != null) {
			return prediction;
		} else {
			return statusHolder.checkStatus(id);
		}
	}
}
