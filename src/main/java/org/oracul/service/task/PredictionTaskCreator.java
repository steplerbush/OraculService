package org.oracul.service.task;

import org.oracul.service.dto.PeriodicalPrediction;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.dto.Prediction3D;
import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.util.IntegrationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miraman on 29.03.2015.
 */
@Service
public class PredictionTaskCreator {

	@Autowired
	private IntegrationFacade facade;

	public enum PredictionType {
		TASK_2D, TASK_3D, PERIODICAL_TASK_2D
	}

	public Long createPrediction(PredictionType type, String... params) throws InterruptedException {
		PredictionTask predictionTask = null;
		Long id = null;
		switch (type) {
		case TASK_2D: {
			id = facade.getPrediction2dRepository().createPrediction(new Prediction2D());
			predictionTask = new PredictionTask2D(id, params, facade);
			break;
		}
		case TASK_3D: {
			id = facade.getPrediction3dRepository().createPrediction(new Prediction3D());
			predictionTask = new PredictionTask3D(id, params, facade);
			break;
		}
		case PERIODICAL_TASK_2D: {
			id = facade.getPeriodicalPredictionRepository().createPrediction(new PeriodicalPrediction());
			predictionTask = new PeriodicalPredictionTask(id, facade, params);
			break;
		}
		}
		return orderPrediction(predictionTask, type);
	}

	public Long orderPrediction(PredictionTask task, PredictionType type) throws InterruptedException {
		try {
			facade.getQueue().addTask(task);
		} catch (InterruptedException ie) {
			throw new InterruptedException();
		}
		facade.getStatusHolder().putStatus(task.getId(), PredictionStatus.IN_ORDER);
		if (facade.getExecutor().getState() == Thread.State.WAITING) {
			facade.getExecutor().resumeExecutor();
		}
		return task.getId();
	}
}
