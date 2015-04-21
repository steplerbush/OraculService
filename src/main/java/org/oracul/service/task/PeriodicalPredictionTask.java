package org.oracul.service.task;

import java.io.File;

import org.apache.log4j.Logger;
import org.oracul.service.dto.PeriodicalPrediction;
import org.oracul.service.util.IntegrationFacade;

public class PeriodicalPredictionTask extends PredictionTask {

	private static final Logger LOGGER = Logger.getLogger(PeriodicalPredictionTask.class);

	public PeriodicalPredictionTask(Long id, IntegrationFacade facade, String... parameters) {
		super(id, parameters, facade);
	}

	@Override
	protected void executePredictionCalculation() {
		String command = facade.getProperty().getExecuteOraculCommandPeriodical();
		String dir = facade.getProperty().getExecuteOraculDirPeriodical();
		try {
			LOGGER.debug("Starting prediction calculation for Periodical task #" + id + " parameters: [DIR= " + dir
					+ ", COMMAND=" + command + " " + parameters[0] + " " + parameters[1] + "]");
			ProcessBuilder processBuilder = new ProcessBuilder(command, id.toString(), parameters[0], parameters[1]);
			processBuilder.directory(new File(dir));
			Process start = processBuilder.start();
			start.waitFor();
			LOGGER.debug("prediction calculation for Periodical task DONE!");
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
			LOGGER.debug("ERROR while executing prediction calculation in peridocal task");
		}
	}

	@Override
	public Integer getCores() {
		return facade.getProperty().getCore2d();
	}

	@Override
	public void run() {
		executePredictionCalculation();
		LOGGER.debug("Execute calculation Periodical task #" + getId());
		PeriodicalPrediction pred = facade.getPeriodicalPredictionRepository().findById(getId());
		pred.setPredictionTimeStep(Integer.parseInt(parameters[1]));
		pred = facade.getPeriodicalBuilder().buildPrediction(pred);
		facade.releaseCores(getCores());
		facade.getPeriodicalPredictionRepository().savePrediction(pred);
		facade.removeStatus(getId());

	}
}
