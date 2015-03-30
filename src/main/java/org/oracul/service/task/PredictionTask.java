package org.oracul.service.task;

import org.oracul.service.util.IntegrationFacade;
import org.oracul.service.util.PropertyHolder;

public abstract class PredictionTask implements Runnable {

    private static long counter = 1;

    protected Long id;
    protected String parameters[];
    PropertyHolder propertyHolder;

    protected IntegrationFacade facade;

    public PredictionTask(String[] parameters, IntegrationFacade facade) {
        this.id = counter++;
        this.parameters = parameters;
        this.propertyHolder = new PropertyHolder();
    }

    public Long getId() {
        return id;
    }

    public abstract Integer getCores();

    protected abstract void executePredictionCalculation();

}
