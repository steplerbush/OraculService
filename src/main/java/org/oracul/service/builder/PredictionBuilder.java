package org.oracul.service.builder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component
public abstract class PredictionBuilder {

	private int uDimension;
	private int vDimension;

	public PredictionBuilder(int uDimension, int vDimension) {
		this.uDimension = uDimension;
		this.vDimension = vDimension;
	}

	protected double[] parseDoubles(File file) {
		try {
			String content = FileUtils.readFileToString(file);
			return Arrays.stream(content.split("\n")).mapToDouble(Double::valueOf).toArray();
		} catch (IOException e) {
			throw new RuntimeException("can't parse file " + file.getName(), e);
		}
	}
	
	public abstract Object  buildPrediction(Object predict);

	public void setuDimension(int uDimension) {
		this.uDimension = uDimension;
	}

	public void setvDimension(int vDimension) {
		this.vDimension = vDimension;
	}

	public int getuDimension() {
		return uDimension;
	}

	public int getvDimension() {
		return vDimension;
	}
}
