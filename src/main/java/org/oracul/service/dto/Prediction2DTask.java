package org.oracul.service.dto;

import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.service.PredictionCalculationService;
import org.oracul.service.util.PredictionExecutor;
import org.oracul.service.util.PredictionResult;
import org.oracul.service.util.StatusPredictionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Prediction2DTask implements Runnable {

	private Long id;

	@Value("${2d.load}")
	private int core;

	@Autowired
	PredictionExecutor executor;

	@Autowired
	PredictionCalculationService calculationService;

	private static long counter;

	@Autowired
	private PredictionBuilder builder;

	@Autowired
	private StatusPredictionHolder statusHolder;

	public Prediction2DTask() {
		this.id = counter++;
	}

	public Long getId() {
		return id;
	}

	@Override
	public void run() {
		executor.load(core);
		statusHolder.putStatus(id, PredictionStatus.PENDING);
	//	calculationService.executeOracul(executeOraculDir3D, executeOraculCommand3D);
		statusHolder.removeStatus(id);
		PredictionResult result = new PredictionResult();
		result.setResult(builder.build3DPrediction());
		executor.unload(core);
	}

}
