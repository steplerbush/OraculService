package org.oracul.service.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.oracul.service.dto.Prediction3D;
import org.oracul.service.repository.Prediction3DRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Prediction3DServiceImpl implements Prediction3DService {

	private static final Logger LOGGER = Logger
			.getLogger(Prediction3DServiceImpl.class);

	@Resource
	public Prediction3DRepository prediction3dRepository;

	@Override
	@Transactional
	public Prediction3D findById(Long id) {
		LOGGER.debug("Trying get prediction3d #" + id + " from DB");
		return prediction3dRepository.findById(id);
	}

	@Override
	@Transactional
	public void savePrediction(Prediction3D prediction) {
		prediction = prediction3dRepository.save(prediction);
		LOGGER.debug("Prediction3D #" + prediction.getId()
				+ " has been saved to DB");
	}

}
