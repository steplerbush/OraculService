package org.oracul.service.util;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.oracul.service.dto.PredictionStatus;
import org.springframework.stereotype.Component;

@Component
public class PredictionsStatusesHolder {
	private static final Logger LOGGER = Logger.getLogger(PredictionsStatusesHolder.class);
	private ConcurrentHashMap<Long, PredictionStatus> statuses = new ConcurrentHashMap<>();

	public PredictionStatus checkStatus(Long id) {
		LOGGER.debug("Checking for status with id=" + id);
		return statuses.get(id);
	}

	public void putStatus(Long id, PredictionStatus status) {
		LOGGER.debug("Status [" + status + "] is putted with id=" + id);
		this.statuses.put(id, status);

	}

	public void removeStatus(Long id) {
		this.statuses.remove(id);
	}
}
