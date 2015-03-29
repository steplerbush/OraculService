package org.oracul.service.worker;

import org.oracul.service.builder.PredictionBuilder2D;
import org.oracul.service.util.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;

public class PredictionTask3D extends PredictionTask {

	private PredictionBuilder2D predictionBuilder;

	@Autowired
	private PropertyHolder property;

	int core = 8;

	@Override
	public void run() {

		executeOracul("dir3d", "com2d");

		// store.putResult(getId(), predictionBuilder.buildPrediction(getId()));
		// statusHolder.removeStatus(getId());
		executor.unload(core);
	}

	public Integer getLoad() {
		return 8;
	}
}
