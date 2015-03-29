package org.oracul.service.worker;

import java.io.IOException;

import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.util.PredictionResult;
import org.springframework.beans.factory.annotation.Value;

public class PredictionTask2D extends PredictionTask {

	@Value("${2d.load}")
	private int core;

	@Value("${oracul.execute.dir.2d}")
	private String executeOraculDir;
	@Value("${oracul.execute.command.2d}")
	private String executeOraculCommand;

	@Override
	public void run() {
		executor.load(core);
		statusHolder.putStatus(getId(), PredictionStatus.PENDING);
		try {
			calculator.executeOracul(executeOraculDir, executeOraculCommand + " " + getId());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		PredictionResult result = new PredictionResult();
		result.setResult(builder.buildPrediction(getId()));
		store.putResult(getId(), result);
		statusHolder.removeStatus(getId());
		executor.unload(core);
	}

	public  int getCores(){return core;}
}
