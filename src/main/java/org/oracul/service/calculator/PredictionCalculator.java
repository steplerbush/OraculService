package org.oracul.service.calculator;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionCalculator {

	@Value("${2d.load}")
	private int core;
	
	

	@Value("${oracul.execute.dir.2d}")
	private String executeOraculDir;
	@Value("${oracul.execute.command.2d}")
	private String executeOraculCommand;

	public void executeOracul(String dir, String command) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File(dir));
		Process start = builder.start();
		start.waitFor();
	}
}
