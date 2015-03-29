package org.oracul.service.util;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.oracul.service.dto.PredictionStatus;
import org.springframework.stereotype.Component;

@Component
public class PredictionStatusHolder {
	private static final Logger LOGGER = Logger.getLogger(PredictionStatusHolder.class);
	private ConcurrentHashMap<Long, PredictionStatus> statuses = new ConcurrentHashMap<>();

	public PredictionStatusHolder() {
		LOGGER.debug("I am created");
	}

	public PredictionStatus checkStatus(Long id) {
		LOGGER.debug("Checking for status with id=" + id);
		return statuses.get(id);
	}

	public void putStatus(Long id, PredictionStatus status) {
		LOGGER.debug("Stutus [" + status + "] is putted with id=" + id);
		this.statuses.put(id, status);

	}

	public void removeStatus(Long id) {
		this.statuses.remove(id);
	}
}
