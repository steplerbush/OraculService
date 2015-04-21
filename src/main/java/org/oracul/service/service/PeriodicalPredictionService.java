package org.oracul.service.service;

import org.oracul.service.dto.PeriodicalPrediction;

public interface PeriodicalPredictionService {
	PeriodicalPrediction findById(Long id);

	PeriodicalPrediction savePrediction(PeriodicalPrediction prediction);
	
	Long createPrediction(PeriodicalPrediction prediction);
	
	void deletePrediction(Long id);
}
