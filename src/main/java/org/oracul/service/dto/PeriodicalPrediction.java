package org.oracul.service.dto;

import java.util.List;

public class PeriodicalPrediction<T> {

	private final List<T> predictions;
	private final int predictionTimeStep;

	public PeriodicalPrediction(List<T> predictions, int predictionTimeStep) {
		this.predictions = predictions;
		this.predictionTimeStep = predictionTimeStep;
	}

	public List<T> getPredictions() {
		return predictions;
	}

	public int getPredictionTimeStep() {
		return predictionTimeStep;
	}
}
