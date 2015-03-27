package org.oracul.service.util;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class PredictionResultStore {

	private HashMap<Long, PredictionResult> results;

	public PredictionResultStore() {
		results = new HashMap<Long, PredictionResult>();
	}

	public void putResult(Long id, PredictionResult task) {
		results.put(id, task);
	}

	public PredictionResult getResult(Long id) {
		return results.get(id);
	}

}
