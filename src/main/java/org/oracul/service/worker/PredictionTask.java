package org.oracul.service.worker;

import org.apache.log4j.Logger;
import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.util.PredictionResultStore;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PredictionTask implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(PredictionTask.class);

	private Long id;
	private static long counter;


	PredictionExecutor executor;
	
	public void setExecutor(PredictionExecutor executor) {
		this.executor = executor;
	}

	private PredictionBuilder builder;

	// @Autowired
	// PredictionStatusHolder statusHolder;

	public void setBuilder(PredictionBuilder builder) {
		this.builder = builder;
	}

	@Autowired
	PredictionResultStore store;

	public PredictionTask() {
		this.id = counter++;
	}

	public Long getId() {
		return id;
	}

	public abstract Integer getLoad();

	public void executeOracul(String dir, String command) {
		try {
			Thread.sleep(15000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		 //ProcessBuilder builder = new ProcessBuilder(command);
		// builder.directory(new File(dir));
		// Process start = builder.start();
		// start.waitFor();
		LOGGER.debug("\nOracul is calculating with task#" + id + " parameters: [DIR= " + dir + ", COMMAND=" + command
				+ "]\n");
	}

}
