package org.oracul.service.repository;

import org.oracul.service.dto.Prediction2D;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Prediction2DRepository extends JpaRepository<Prediction2D, Long> {
	Prediction2D findById(Long id);
}

