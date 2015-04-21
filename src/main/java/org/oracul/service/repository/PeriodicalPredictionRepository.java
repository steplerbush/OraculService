package org.oracul.service.repository;

import org.oracul.service.dto.PeriodicalPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodicalPredictionRepository extends JpaRepository<PeriodicalPrediction, Long> {
	PeriodicalPrediction findById(Long id);
}

