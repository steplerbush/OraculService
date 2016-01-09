package org.oracul.service.service;

import org.oracul.service.dto.ImagePrediction;

import java.util.UUID;

public interface ImagePredictionService {
	ImagePrediction findById(UUID id);

	ImagePrediction savePrediction(ImagePrediction prediction);
	
	UUID createPrediction(ImagePrediction prediction);
	
	void deletePrediction(UUID id);
}
