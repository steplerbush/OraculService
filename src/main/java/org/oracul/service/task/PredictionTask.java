package org.oracul.service.task;

import org.oracul.service.util.IntegrationFacade;
import org.oracul.service.util.PropertyHolder;

public abstract class PredictionTask implements Runnable {


	protected Long id;
	protected String parameters[];
	

	protected IntegrationFacade facade;

	public PredictionTask(Long id, String[] parameters, IntegrationFacade facade) {
		this.id = id;
		this.parameters = parameters;
		this.facade = facade;
	}

	public Long getId() {
		return id;
	}

	public abstract Integer getCores();

	protected abstract void executePredictionCalculation();

}
