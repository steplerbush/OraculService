package org.oracul.service.util;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.oracul.service.worker.PredictionTask2D;
import org.springframework.stereotype.Component;

@Component
public class PredictionResultStore {
	private static final Logger LOGGER = Logger.getLogger(PredictionResultStore.class);

	private HashMap<Long, Object> results;

	public PredictionResultStore() {
		results = new HashMap<Long, Object>();
	}

	public void putResult(Long id, Object task) {
		LOGGER.debug("Prediction was putted with id=" + id);
		results.put(id, task);
	}

	public Object getResult(Long id) {
		return results.get(id);
	}

}
