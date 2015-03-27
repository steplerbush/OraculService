package org.oracul.service.util;

import java.util.concurrent.ConcurrentHashMap;

import org.oracul.service.dto.PredictionStatus;
import org.springframework.stereotype.Component;

@Component
public class StatusPredictionHolder {
	ConcurrentHashMap<Long, PredictionStatus> statuses;

	public StatusPredictionHolder() {
		statuses = new ConcurrentHashMap<>();
	}

	public PredictionStatus checkStatus(Long id) {
		return statuses.get(id);
	}

	public void putStatus(Long id, PredictionStatus status) {
		this.statuses.put(id, status);
	}

	public void removeStatus(Long id) {
		this.statuses.remove(id);
	}
}
