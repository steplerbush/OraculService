package org.oracul.service.task;

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
        TASK_2D, TASK_3D
    }

    public Long createPrediction(PredictionType type, String[] params) throws InterruptedException {
        PredictionTask predictionTask;
        switch (type) {
            case TASK_2D: {
                predictionTask = new PredictionTask2D(params, facade);
                break;
            }
            case TASK_3D: {
                predictionTask = new PredictionTask3D(params, facade);
                break;
            }
            default: {
                predictionTask = null;
            }
        }
        return orderPrediction(predictionTask);
    }

    public Long orderPrediction(PredictionTask task) throws InterruptedException {
        facade.getQueue().addTask(task);
        facade.getStatusHolder().putStatus(task.getId(), PredictionStatus.IN_ORDER);
        if (facade.getExecutor().getState() == Thread.State.WAITING) {
            facade.getExecutor().resumeExecutor();
        }
        return task.getId();
    }
}
