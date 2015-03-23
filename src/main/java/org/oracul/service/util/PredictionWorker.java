package org.oracul.service.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.dto.PredictionStatus;
import org.oracul.service.dto.PredictionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PredictionWorker extends Thread {

	private static final Logger logger = Logger.getLogger(PredictionWorker.class);

	@Autowired
	private TaskQueue queue;

	@Autowired
	private PredictionResultHolder results;

	@Autowired
	private PredictionBuilder builder;

	@Autowired
	private StatusTaskHolder statusHolder;

	@Value("${oracul.execute.dir.3d}")
	private String executeOraculDir3D;

	@Value("${oracul.execute.command.3d}")
	private String executeOraculCommand3D;

	public PredictionWorker() {
		this.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
			}
			if (!queue.isEmpty()) {
				try {
					PredictionTask task = queue.getTask();
					statusHolder.putStatus(task.getId(), PredictionStatus.PENDING);
					// executeOracul(executeOraculDir3D,
					// executeOraculCommand3D);
					Thread.sleep(10000);
					statusHolder.removeStatus(task.getId());
					PredictionResult result = new PredictionResult();
					result.setResult(builder.build3DPrediction());
					results.putResult(task.getId(), result);
				} catch (Exception e) {
					logger.error("failed to execute oracul", e);
				}
			}
		}

	}

	private void executeOracul(String dir, String command) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File(dir));
		Process start = builder.start();
		start.waitFor();
		if (logger.isDebugEnabled()) {
			logger.debug("executed oracul");
		}
	}
}
