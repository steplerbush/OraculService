package org.oracul.service.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.dto.PeriodicalPrediction;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PeriodicalPredictionController {
	private static final Logger logger = Logger
			.getLogger(PeriodicalPredictionController.class);

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
	public synchronized PeriodicalPrediction<Prediction2D> getPrediction(
			@PathVariable int predictionTimeStep,
			@PathVariable int predictionCount) {
		Validate.isTrue(predictionCount > 0, "predictionCount must be positive");
		Validate.isTrue(predictionTimeStep > 0,
				"predictionTimeStep must be positive");
		Validate.isTrue(
				predictionCount * predictionTimeStep <= maxPredictionSeconds,
				"maximum supported prediction is " + maxPredictionSeconds
						/ 3600 + " hours");
		Validate.isTrue(predictionCount <= maxPredictionCount, "can return "
				+ maxPredictionCount + " predictions at most");

		try {
			// for now lets re-calculate each prediction from scratch. Shouldn't
			// take much time but this way I won't
			// change C++ code
			List<Prediction2D> predictions = new ArrayList<>(predictionCount);
			for (int i = 1; i <= predictionCount; i++) {
				if (logger.isDebugEnabled()) {
					logger.debug("executing periodical oracul iteration=" + i
							+ " of " + predictionCount);
				}
				int predictionTime = i * predictionTimeStep;
				executeOracul(executeOraculDir, executeOraculCommand,
						predictionTime);
				predictions.add(builder.build2DPrediction(new File(
						singletonUpath), new File(singletonVpath)));
			}
			return new PeriodicalPrediction<Prediction2D>(predictions,
					predictionTimeStep);
		} catch (Exception e) {
			logger.error("failed to execute oracul", e);
			throw new RuntimeException(e);
		}

	}

	private void executeOracul(String dir, String command, int predictionTime)
			throws Exception {
		ProcessBuilder builder = new ProcessBuilder(command,
				String.valueOf(predictionTime));
		builder.directory(new File(dir));
		Process start = builder.start();
		start.waitFor();

		if (logger.isDebugEnabled()) {
			logger.debug("executed oracul with predictionTime="
					+ predictionTime);
		}
	}

}
