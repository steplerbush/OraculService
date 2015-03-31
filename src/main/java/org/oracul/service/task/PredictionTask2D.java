package org.oracul.service.task;

import org.apache.log4j.Logger;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.util.IntegrationFacade;

public class PredictionTask2D extends PredictionTask {

	private static final Logger LOGGER = Logger.getLogger(PredictionTask2D.class);

	public PredictionTask2D(String[] parameters, IntegrationFacade facade) {
		super(parameters, facade);
	}

	@Override
	protected void executePredictionCalculation() {
		String command = facade.getProperty().getExecuteOraculCommand2D();
		String dir = facade.getProperty().getExecuteOraculDir2D();
		try {
			LOGGER.debug("Execute calculation 2D task: test sleep for 10 sec");
			Thread.sleep(10000);
			LOGGER.debug("Starting prediction calculation for 2D task #" + id + " parameters: [DIR= " + dir
					+ ", COMMAND=" + command + "]");
			// ProcessBuilder processBuilder = new ProcessBuilder(command);
			// processBuilder.directory(new File(dir));
			// Process start = processBuilder.start();
			// start.waitFor();
		} catch (Exception e) {
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
		LOGGER.debug("Execute calculation 2D task#" + getId() + ": test sleep for 10 sec");
		LOGGER.debug("For testing getting builder" + facade.getBuilder2D());
		Prediction2D pred = facade.getBuilder2D().buildPrediction(getId());
		facade.putResult(getId(), pred);
		facade.getPredictionRepository().savePrediction(pred);
		facade.removeStatus(getId());
		facade.releaseCores(getCores());
	}
}
