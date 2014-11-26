package org.oracul.service.builder;

import org.apache.commons.io.FileUtils;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Component
public class PredictionBuilder {

	@Value("${2d.size.u}")
	private int uDimension;
	@Value("${2d.size.v}")
	private int vDimension;

	public Prediction2D build2DPrediction(File uValuesFile, File vValuesFile) {
		double[] u = parseDoubles(uValuesFile);
		double[] v = parseDoubles(vValuesFile);
		return new Prediction2D(u, v, uDimension, vDimension);
	}

	private double[] parseDoubles(File file) {
		try {
			String content = FileUtils.readFileToString(file);
			return Arrays.stream(content.split("\n")).mapToDouble(Double::valueOf).toArray();
		} catch (IOException e) {
			throw new RuntimeException("can't parse file " + file.getName(), e);
		}
	}


	public void setuDimension(int uDimension) {
		this.uDimension = uDimension;
	}

	public void setvDimension(int vDimension) {
		this.vDimension = vDimension;
	}



}


