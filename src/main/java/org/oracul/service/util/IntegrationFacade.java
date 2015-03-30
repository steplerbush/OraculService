package org.oracul.service.util;

import org.oracul.service.builder.PredictionBuilder2D;
import org.oracul.service.builder.PredictionBuilder3D;
import org.oracul.service.executor.PredictionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miraman on 30.03.2015.
 */
@Service
public class IntegrationFacade {

    @Autowired
    private PredictionsStatusesHolder statusHolder;

    @Autowired
    private PredictionExecutor executor;

    @Autowired
    private PredictionQueue queue;

    @Autowired
    private PredictionBuilder2D builder2D;

    @Autowired
    private PredictionBuilder3D builder3D;

    @Autowired
    private PredictionsResultsHolder resultsHolder;

    public PredictionsStatusesHolder getStatusHolder() {
        return statusHolder;
    }

    public PredictionExecutor getExecutor() {
        return executor;
    }

    public PredictionQueue getQueue() {
        return queue;
    }

    public PredictionBuilder2D getBuilder2D() {
        return builder2D;
    }

    public PredictionBuilder3D getBuilder3D() {
        return builder3D;
    }

    public void putResult(Long id, Object result) {
        resultsHolder.putResult(id, result);
    }

    public void removeStatus(Long id) {
        statusHolder.removeStatus(id);
    }

    public void releaseCores(Integer cores) {
        executor.releaseCores(cores);
    }

}
