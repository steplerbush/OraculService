package org.oracul.service.service;

import org.apache.log4j.Logger;
import org.oracul.service.dto.ImagePrediction;
import org.oracul.service.repository.ImagePredictionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;


@Service
public class ImagePredictionServiceImpl implements ImagePredictionService {

	private static final Logger LOGGER = Logger.getLogger(ImagePredictionServiceImpl.class);

	@Resource
	public ImagePredictionRepository predictionRepository;

	@Override
	@Transactional
	public ImagePrediction findById(UUID id) {
		LOGGER.debug("Trying get ImagePrediction #" + id + " from DB");
		return predictionRepository.findById(id);
	}

	@Override
	@Transactional
	public ImagePrediction savePrediction(ImagePrediction prediction) {
		ImagePrediction predict = predictionRepository.save(prediction);
		LOGGER.debug("Prediction#" + prediction.getId() + " has been saved to DB");
		return predict;
	}

	@Override
	@Transactional
	public UUID createPrediction(ImagePrediction prediction) {
		ImagePrediction predict = predictionRepository.save(prediction);
		return predict.getId();
	}

	@Override
	@Transactional
	public void deletePrediction(UUID id) {
		predictionRepository.delete(id);
	}

}
