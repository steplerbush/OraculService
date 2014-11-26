package org.oracul.service.controller;

import org.apache.log4j.Logger;
import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class SingletonPrognosisController {

	private static final Logger logger = Logger.getLogger(SingletonPrognosisController.class);

	@Autowired
	private PredictionBuilder builder;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${oracul.execute.dir}")
	private String executeOraculDir;
	@Value("${oracul.execute.command}")
	private String executeOraculCommand;

	@Value("${oracul.singleton-results.u}")
	private String singletonUpath;
	@Value("${oracul.singleton-results.v}")
	private String singletonVpath;

	@RequestMapping(value = "/singleton", method = RequestMethod.GET)
	public synchronized Prediction2D getMockPrediction() {
		try {
			executeOracul();
			return buildResults();
		} catch (Exception e) {
			logger.error("failed to execute oracul", e);
			throw new RuntimeException(e);
		}

	}

	private Prediction2D buildResults() {
		File uValuesFile = new File(singletonUpath);
		File vValuesFile = new File(singletonVpath);
		return builder.build2DPrediction(uValuesFile, vValuesFile);
	}

	private void executeOracul() throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(executeOraculCommand);
		builder.directory(new File(executeOraculDir));
		Process start = builder.start();
		start.waitFor();

		if (logger.isDebugEnabled()) {
			logger.debug("executed oracul");
		}
	}
}
