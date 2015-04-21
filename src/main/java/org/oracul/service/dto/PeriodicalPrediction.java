package org.oracul.service.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class PeriodicalPrediction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "periodicalPrediction", cascade = CascadeType.ALL)
	private List<Prediction2D> predictions;
	private int predictionTimeStep;

	public PeriodicalPrediction() {

	}

	public PeriodicalPrediction(List<Prediction2D> predictions, int predictionTimeStep) {
		this.predictions = predictions;
		this.predictionTimeStep = predictionTimeStep;
	}

	public List<Prediction2D> getPredictions() {
		return predictions;
	}

	public int getPredictionTimeStep() {
		return predictionTimeStep;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPredictions(List<Prediction2D> predictions) {
		this.predictions = predictions;
	}

	public void setPredictionTimeStep(int predictionTimeStep) {
		this.predictionTimeStep = predictionTimeStep;
	}
	
}
