package org.oracul.service.builder;

import org.springframework.stereotype.Component;

@Component
public interface Builder<T> {
	T buildPrediction(Long id);
}
