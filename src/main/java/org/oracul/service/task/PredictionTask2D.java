package org.oracul.service.task;

import java.io.File;

import org.apache.log4j.Logger;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.util.IntegrationFacade;

public class PredictionTask2D extends PredictionTask {

	private static final Logger LOGGER = Logger.getLogger(PredictionTask2D.class);

	public PredictionTask2D(Long id, String[] parameters, IntegrationFacade facade) {
		super(id, parameters, facade);
	}

	@Override
	protected void executePredictionCalculation() {
		String command = facade.getProperty().getExecuteOraculCommand2D();
		String dir = facade.getProperty().getExecuteOraculDir2D();
		try {
						LOGGER.debug("Starting prediction calculation for 2D task #" + id + " parameters: [DIR= " + dir
					+ ", COMMAND=" + command +  " " + id + "]");
			 ProcessBuilder processBuilder = new ProcessBuilder(command , id.toString());
			 LOGGER.debug("ProcessBuilder created");
			 LOGGER.debug("Directory " + dir + " exist" +new File(dir).exists());
			 processBuilder.directory(new File(dir));
			 LOGGER.debug("Directory added");
			 Process start = processBuilder.start();
			 LOGGER.debug("Process started");
			 start.waitFor();
		} catch (Exception e) {
			LOGGER.debug("ERROR here");
			LOGGER.debug(e);
			LOGGER.debug(e.getMessage());
			LOGGER.debug("ERROR while executing prediction calculation in 2D task");
		}
	}

	@Override
	public Integer getCores() {
		return facade.getProperty().getCore2d();
	}

	@Override
	public void run() {
		executePredictionCalculation();
		LOGGER.debug("Execute calculation 2D task#" + getId());
		LOGGER.debug("For testing getting builder" + facade.getBuilder2D());
		Prediction2D pred = facade.getBuilder2D().buildPrediction(getId());
		facade.putResult(getId(), pred);
		facade.getPrediction2dRepository().savePrediction(pred);
		facade.removeStatus(getId());
		facade.releaseCores(getCores());
	}
}
