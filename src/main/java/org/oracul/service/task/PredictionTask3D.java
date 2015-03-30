package org.oracul.service.task;

import org.apache.log4j.Logger;
import org.oracul.service.util.IntegrationFacade;
import org.oracul.service.util.PropertyHolder;

import java.io.File;

public class PredictionTask3D extends PredictionTask {

    private static final Logger LOGGER = Logger.getLogger(PredictionTask3D.class);

    public PredictionTask3D(String[] parameters, IntegrationFacade facade) {
        super(parameters, facade);
    }

    @Override
    protected void executePredictionCalculation() {
        String command = facade.getProperty().getExecuteOraculCommand3D();
        String dir = facade.getProperty().getExecuteOraculDir3D();
        try {
            LOGGER.debug("Execute calculation 3D task: test sleep for 10 sec");
            Thread.sleep(10000);
            LOGGER.debug("Starting prediction calculation for 3D task #" + id + " parameters: [DIR= " + dir
                    + ", COMMAND=" + command + "]");
            ProcessBuilder processBuilder = new ProcessBuilder(command);
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
        LOGGER.debug("Execute calculation 3D task#" + getId() + ": test sleep for 10 sec");
        facade.putResult(getId(), facade.getBuilder3D().buildPrediction(getId()));
        facade.removeStatus(getId());
        facade.releaseCores(getCores());
    }
}
