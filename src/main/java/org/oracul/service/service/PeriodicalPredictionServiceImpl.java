package org.oracul.service.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.oracul.service.dto.PeriodicalPrediction;
import org.oracul.service.repository.PeriodicalPredictionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PeriodicalPredictionServiceImpl implements PeriodicalPredictionService {

	private static final Logger LOGGER = Logger.getLogger(PeriodicalPredictionServiceImpl.class);

	@Resource
	public PeriodicalPredictionRepository predictionRepository;

	@Override
	@Transactional
	public PeriodicalPrediction findById(Long id) {
		LOGGER.debug("Trying get PeriodicalPrediction#" + id + " from DB");
		return predictionRepository.findById(id);
	}

	@Override
	@Transactional
	public PeriodicalPrediction savePrediction(PeriodicalPrediction prediction) {
		PeriodicalPrediction periodicalPrediction = predictionRepository.save(prediction);
		LOGGER.debug("PeriodicalPrediction#" + prediction.getId() + " has been saved to DB");
		return periodicalPrediction;
	}

	@Override
	@Transactional
	public Long createPrediction(PeriodicalPrediction prediction) {
		PeriodicalPrediction periodicalPrediction = predictionRepository.save(prediction);
		return periodicalPrediction.getId(); 
	}

	@Override
	@Transactional
	public void deletePrediction(Long id) {
		predictionRepository.delete(id);
	}

}
