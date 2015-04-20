package org.oracul.service.task;

import java.io.File;

import org.apache.log4j.Logger;
import org.oracul.service.dto.Prediction3D;
import org.oracul.service.util.IntegrationFacade;

public class PredictionTask3D extends PredictionTask {

	private static final Logger LOGGER = Logger
			.getLogger(PredictionTask3D.class);

	public PredictionTask3D(Long id, String[] parameters,
			IntegrationFacade facade) {
		super(id, parameters, facade);
	}

	@Override
	protected void executePredictionCalculation() {
		String command = facade.getProperty().getExecuteOraculCommand3D();
		String dir = facade.getProperty().getExecuteOraculDir3D();
		try {
			LOGGER.debug("Starting prediction calculation for 3D task #" + id
					+ " parameters: [DIR= " + dir + ", COMMAND=" + command
					+ "]");
			 ProcessBuilder processBuilder = new ProcessBuilder(command , id.toString());
			 processBuilder.directory(new File(dir));
			 Process start = processBuilder.start();
			 start.waitFor();
		} catch (Exception e) {
			LOGGER.debug("ERROR while executing prediction calculation in 3D task");
		}
	}

	@Override
	public Integer getCores() {
		return facade.getProperty().getCore3d();
	}

	@Override
	public void run() {
		executePredictionCalculation();
		LOGGER.debug("Execute calculation 3D task #" + getId()
				+ ": test sleep for 10 sec");
		Prediction3D pred = facade.getBuilder3D().buildPrediction(getId());
		facade.putResult(getId(), pred);
		facade.releaseCores(getCores());
		facade.getPrediction3dRepository().savePrediction(pred);
		facade.removeStatus(getId());

	}
}
