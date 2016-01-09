package org.oracul.service.repository;

import org.oracul.service.dto.ImagePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImagePredictionRepository extends JpaRepository<ImagePrediction, UUID> {
	ImagePrediction findById(UUID id);
}

