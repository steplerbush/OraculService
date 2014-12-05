package org.oracul.service.dto;

import java.util.List;

public class PeriodicalPrediction2D {

	private final List<Prediction2D> predictions;
	private final int predictionTimeStep;

	public PeriodicalPrediction2D(List<Prediction2D> predictions, int predictionTimeStep) {
		this.predictions = predictions;
		this.predictionTimeStep = predictionTimeStep;
	}

	public List<Prediction2D> getPredictions() {
		return predictions;
	}

	public int getPredictionTimeStep() {
		return predictionTimeStep;
	}
}
