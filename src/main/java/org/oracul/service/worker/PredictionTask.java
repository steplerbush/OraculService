package org.oracul.service.worker;

import org.oracul.service.builder.PredictionBuilder2D;
import org.oracul.service.calculator.PredictionCalculator;
import org.oracul.service.executor.PredictionExecutor;
import org.oracul.service.util.PredictionResultStore;
import org.oracul.service.util.StatusPredictionHolder;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PredictionTask implements Runnable {

	private Long id;
	private static long counter;

	@Autowired
	PredictionExecutor executor;

	@Autowired
	PredictionBuilder2D builder;

	@Autowired
	StatusPredictionHolder statusHolder;

	@Autowired
	PredictionCalculator calculator;

	@Autowired
	PredictionResultStore store;

	public PredictionTask() {
		this.id = counter++;
	}

	public Long getId() {
		return id;
	}

	public abstract int getCores();

}
