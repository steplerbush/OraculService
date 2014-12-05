package org.oracul.service.controller;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.oracul.service.dto.PeriodicalPrediction2D;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PeriodicalPredictionController {
	private static final Logger logger = Logger.getLogger(PeriodicalPredictionController.class);

	@Value("${oracul.periodical.maxPredictionSeconds}")
	private int maxPredictionSeconds;

	@Value("${oracul.periodical.maxPredictionCount}")
	private int maxPredictionCount;

	@Autowired
	private ResourceLoader resourceLoader;


	@RequestMapping(value = "/periodical/{predictionCount}/{predictionTimeStep}", method = RequestMethod.GET)
	public PeriodicalPrediction2D getMockPrediction(@PathVariable int predictionTimeStep, @PathVariable int predictionCount) {
		Validate.isTrue(predictionCount > 0 ,"predictionCount must be positive");
		Validate.isTrue(predictionTimeStep > 0 ,"predictionTimeStep must be positive");
		Validate.isTrue(predictionCount * predictionTimeStep <= maxPredictionSeconds,
				"maximum supported prediction is " + maxPredictionSeconds / 3600 + " hours");
		Validate.isTrue(predictionCount <= maxPredictionCount, "can return " + maxPredictionCount + " predictions at most");
		try {
//			executeOracul();
			return buildResults(predictionCount, predictionTimeStep);
		} catch (Exception e) {
			logger.error("failed to execute oracul", e);
			throw new RuntimeException(e);
		}

	}

	private PeriodicalPrediction2D buildResults(int predictionCount, int predictionTimeStep) {
		Prediction2D mock2D = new Prediction2D(new double[]{1.}, new double[]{2.}, 1, 1);
		List<Prediction2D> predictions = new ArrayList<>(predictionCount);
		for (int i = 0; i < predictionCount; i++) {

			predictions.add(mock2D);
		}
		return new PeriodicalPrediction2D(predictions, predictionTimeStep);
	}

}
