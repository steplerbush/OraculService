package org.oracul.service.builder;

import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class MockPredictionBuilder {
	@Value("60")
	private int uDimension;
	@Value("61")
	private int vDimension;

	@Value("src/main/resources/mockData/u.out")
	private String uValuesPath;

	@Value("src/main/resources/mockData/v.out")
	private String vValuesPath;

	public Prediction2D build2DPrediction() {
		double[] u = parseDoubles(uValuesPath);
		double[] v = parseDoubles(vValuesPath);
		return new Prediction2D(u, v, uDimension, vDimension);
	}

	private double[] parseDoubles(String pathToFile) {
		try {
			String content = new String(Files.readAllBytes(Paths.get(pathToFile)));
			return Arrays.stream(content.split("\n")).mapToDouble(Double::valueOf).toArray();
		} catch (IOException e) {
			throw new RuntimeException("can't parse file " + pathToFile, e);
		}
	}


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

	public String getuValuesPath() {
		return uValuesPath;
	}

	public void setuValuesPath(String uValuesPath) {
		this.uValuesPath = uValuesPath;
	}

	public String getvValuesPath() {
		return vValuesPath;
	}

	public void setvValuesPath(String vValuesPath) {
		this.vValuesPath = vValuesPath;
	}
}


