package org.oracul.service.executor;

import org.apache.log4j.Logger;
import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.util.PredictionQueue;
import org.oracul.service.util.PredictionsStatusesHolder;
import org.oracul.service.task.PredictionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PredictionExecutor extends Thread {

    private static final Logger LOGGER = Logger.getLogger(PredictionExecutor.class);

    @Autowired
    private PredictionExecutorPool predictionPool;

    @Autowired
    private PredictionQueue predictionQueue;

    @Autowired
    private PredictionsStatusesHolder statusHolder;

    private AtomicInteger currentLoad = new AtomicInteger();

    @Value("${service.pool.size}")
    private Integer maxLoaded;

    private boolean suspendFlag;

    public PredictionExecutor() {
        setDaemon(true);
    }

    public void loadCores(Integer coresCount) {
        currentLoad.addAndGet(coresCount);
    }

    public void releaseCores(Integer coresCount) {
        currentLoad.addAndGet(-coresCount);
    }

    public synchronized void suspendExecutor() {
        LOGGER.debug("Executor is suspeding.");
        suspendFlag = true;
    }

    public synchronized void resumeExecutor() {
        LOGGER.debug("Executor is resuming.");
        suspendFlag = false;
        notify();
    }

    public void run() {
        while (!isInterrupted()) {
            checkIsEmptyQueue();
            waitIfSuspended();
            if ((currentLoad.get() + predictionQueue.getNextLoad()) <= maxLoaded) {
                PredictionTask task = predictionQueue.getTask();
                LOGGER.debug("***Current number of used cores: " + currentLoad + "; Max cores avalible: "
                        + maxLoaded + "***");
                LOGGER.debug("***Executor gets Task from Queue: Task #" + task.getId() + "; needed cores: "
                        + task.getCores() + "***");
                statusHolder.putStatus(task.getId(), PredictionStatus.PENDING);
                loadCores(task.getCores());
                predictionPool.executePrediction(task);
            } else {
                LOGGER.debug("WAITING. SYSTEM IS OVERLOAD.");
            }
        }
    }

    private void checkIsEmptyQueue() {
        if (predictionQueue.isEmpty()) {
            LOGGER.debug("Checking queue. Queue is empty.");
            suspendExecutor();
        }
    }

    private void waitIfSuspended() {
        synchronized (this) {
            while (suspendFlag) {
                try {
                    LOGGER.debug("Executor is waiting for tasks");
                    wait();
                } catch (InterruptedException e) {
                    LOGGER.debug("Executor ERROR while waiting for tasks");
                }
            }
        }
    }
}
