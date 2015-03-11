package org.oracul.service.controller;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.dto.Prediction3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingletonPrognosisController {

	private static final Logger logger = Logger
			.getLogger(SingletonPrognosisController.class);

	@Autowired
	private PredictionBuilder builder;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${oracul.execute.dir}")
	private String executeOraculDir;
	@Value("${oracul.execute.command}")
	private String executeOraculCommand;

	@Value("${oracul.execute.dir.3d}")
	private String executeOraculDir3D;
	@Value("${oracul.execute.command.3d}")
	private String executeOraculCommand3D;

	@Value("${oracul.singleton-results.u}")
	private String singletonUpath;
	@Value("${oracul.singleton-results.v}")
	private String singletonVpath;

	@RequestMapping(value = "/singleton", method = RequestMethod.GET)
	public synchronized Prediction2D getMockPrediction() {
		try {
			executeOracul(executeOraculDir, executeOraculCommand);
			return builder.build2DPrediction(new File(singletonVpath),
					new File(singletonUpath));
		} catch (Exception e) {
			logger.error("failed to execute oracul", e);
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "/singleton3D", method = RequestMethod.GET)
	public synchronized Prediction3D get3DPrediction() {
		try {
			executeOracul(executeOraculDir3D, executeOraculCommand3D);
			return builder.build3DPrediction();
		} catch (Exception e) {
			logger.error("failed to execute oracul", e);
			throw new RuntimeException(e);
		}

	}

	private void executeOracul(String dir, String command) throws IOException,
			InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File(dir));
		Process start = builder.start();
		start.waitFor();

		if (logger.isDebugEnabled()) {
			logger.debug("executed oracul");
		}
	}
}
