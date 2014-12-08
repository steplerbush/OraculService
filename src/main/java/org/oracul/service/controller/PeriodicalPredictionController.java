package org.oracul.service.controller;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.dto.PeriodicalPrediction2D;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PeriodicalPredictionController {
	private static final Logger logger = Logger.getLogger(PeriodicalPredictionController.class);

	@Value("${oracul.periodical.maxPredictionSeconds}")
	private int maxPredictionSeconds;

	@Value("${oracul.periodical.maxPredictionCount}")
	private int maxPredictionCount;

	@Value("${oracul.periodical.execute.dir}")
	private String executeOraculDir;
	@Value("${oracul.periodical.execute.command}")
	private String executeOraculCommand;
	@Value("${oracul.periodical.singleton-results.u}")
	private String singletonUpath;
	@Value("${oracul.periodical.singleton-results.v}")
	private String singletonVpath;

	@Autowired
	private PredictionBuilder builder;

	@Autowired
	private ResourceLoader resourceLoader;


	@RequestMapping(value = "/periodical/{predictionCount}/{predictionTimeStep}", method = RequestMethod.GET)
	public synchronized PeriodicalPrediction2D getMockPrediction(@PathVariable int predictionTimeStep, @PathVariable int predictionCount) {
		Validate.isTrue(predictionCount > 0, "predictionCount must be positive");
		Validate.isTrue(predictionTimeStep > 0, "predictionTimeStep must be positive");
		Validate.isTrue(predictionCount * predictionTimeStep <= maxPredictionSeconds,
				"maximum supported prediction is " + maxPredictionSeconds / 3600 + " hours");
		Validate.isTrue(predictionCount <= maxPredictionCount, "can return " + maxPredictionCount + " predictions at most");

		try {
			// for now lets re-calculate each prediction from scratch. Shouldn't take much time but this way I won't
			// change C++ code
			List<Prediction2D> predictions = new ArrayList<>(predictionCount);
			for (int i = 1; i <= predictionCount; i++) {
				if (logger.isDebugEnabled()) {
					logger.debug("executing periodical oracul iteration=" + i + " of " + predictionCount);
				}
				int predictionTime = i * predictionTimeStep;
				executeOracul(predictionTime);
				predictions.add(parseResults());
			}
			return new PeriodicalPrediction2D(predictions, predictionTimeStep);
		} catch (Exception e) {
			logger.error("failed to execute oracul", e);
			throw new RuntimeException(e);
		}

	}

	private Prediction2D parseResults() {
		File uValuesFile = new File(singletonUpath);
		File vValuesFile = new File(singletonVpath);
		return builder.build2DPrediction(uValuesFile, vValuesFile);
	}

	private void executeOracul(int predictionTime) throws Exception {
		ProcessBuilder builder = new ProcessBuilder(executeOraculCommand, String.valueOf(predictionTime));
		builder.directory(new File(executeOraculDir));
		Process start = builder.start();
		start.waitFor();

		if (logger.isDebugEnabled()) {
			logger.debug("executed oracul with predictionTime=" + predictionTime);
		}
	}


}
