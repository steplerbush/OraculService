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

	@RequestMapping(value = "/singleton", method = RequestMethod.GET)
	public synchronized Prediction2D getMockPrediction() {
		try {
			ProcessBuilder builder = new ProcessBuilder(executeOraculCommand);
			builder.directory(new File(executeOraculDir));
			Process start = builder.start();
			start.waitFor();

//			ProcessBuilder builder = new ProcessBuilder("./sleep.sh");
//			builder.directory(new File("/Users/metzgermeister/temp/"));
//			Process start = builder.start();
//			start.waitFor();

			if (logger.isDebugEnabled()) {
				logger.debug("executed oracul");
			}
		} catch (Exception e) {
			logger.error("failed to execute oracul", e);
			throw new RuntimeException(e);
		}
		return new Prediction2D(new double[]{42}, new double[]{42}, 1, 1);
	}
}
