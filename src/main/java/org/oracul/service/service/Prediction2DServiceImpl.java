package org.oracul.service.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.repository.Prediction2DRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class Prediction2DServiceImpl implements Prediction2DService {

	private static final Logger LOGGER = Logger.getLogger(Prediction2DServiceImpl.class);

	@Resource
	public Prediction2DRepository predictionRepository;

	@Override
	@Transactional
	public Prediction2D findById(Long id) {
		LOGGER.debug("Trying get prediction2d#" + id + " from DB");
		return predictionRepository.findById(id);
	}

	@Override
	@Transactional
	public Prediction2D savePrediction(Prediction2D prediction) {
		Prediction2D prediction2d = predictionRepository.save(prediction);
		LOGGER.debug("Prediction2D#" + prediction.getId() + " has been saved to DB");
		return prediction2d;
	}

}
