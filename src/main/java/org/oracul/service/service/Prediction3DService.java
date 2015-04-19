package org.oracul.service.service;

import org.oracul.service.dto.Prediction3D;

public interface Prediction3DService {
	Prediction3D findById(Long id);

	void savePrediction(Prediction3D prediction);
	
	Long createPrediction(Prediction3D prediction);
	
	void deletePrediction(Long id);
}
