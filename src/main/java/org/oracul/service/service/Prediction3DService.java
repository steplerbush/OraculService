package org.oracul.service.service;

import org.oracul.service.dto.Prediction3D;

public interface Prediction3DService {
	Prediction3D findById(Long id);

	Prediction3D savePrediction(Prediction3D prediction);
}
