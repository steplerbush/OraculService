package org.oracul.service.util;

import java.util.HashMap;
import java.util.LinkedList;

import org.oracul.service.dto.PredictionTask;
import org.springframework.stereotype.Component;

@Component
public class PredictionResultHolder {

	private HashMap<Long, PredictionResult> results;

	public PredictionResultHolder() {
		results = new HashMap<Long, PredictionResult>();
	}

	public void putResult(Long id, PredictionResult task) {
		results.put(id, task);
	}

	public PredictionResult getResult(Long id) {
		return results.get(id);
	}

}
