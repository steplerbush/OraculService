package org.oracul.service.repository;

import org.oracul.service.dto.Prediction3D;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Prediction3DRepository extends JpaRepository<Prediction3D, Long> {
	Prediction3D findById(Long id);
}
