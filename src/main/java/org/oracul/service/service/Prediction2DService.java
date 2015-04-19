package org.oracul.service.service;

import org.oracul.service.dto.Prediction2D;

public interface Prediction2DService {
	Prediction2D findById(Long id);

	Prediction2D savePrediction(Prediction2D prediction);
	
	Long createPrediction(Prediction2D prediction);
	
	void deletePrediction(Long id);
}
