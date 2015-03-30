package org.oracul.service.util;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PredictionsResultsHolder {
	private static final Logger LOGGER = Logger.getLogger(PredictionsResultsHolder.class);

	private HashMap<Long, Object> results;

	public PredictionsResultsHolder() {
		results = new HashMap<>();
	}

	public void putResult(Long id, Object result) {
		LOGGER.debug("Prediction was putted with id=" + id);
		results.put(id, result);
	}

	public Object getResult(Long id) {
		LOGGER.debug("Getting result with id=" + id);
		return results.get(id);
	}

}
