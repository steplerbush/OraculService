package org.oracul.service.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.dto.Prediction3D;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionBuilder {

	@Value("${2d.size.u}")
	private int uDimension;
	@Value("${2d.size.v}")
	private int vDimension;

	@Value("${3d.size.u}")
	private int u3Dimension;
	@Value("${3d.size.v}")
	private int v3Dimension;

	@Value("${oracul.root.output.3d}")
	private String path;

	@Value("${type.filter}")
	private String typeFilter;

	@Value("${parameter.count}")
	private String parameterCount;

	public Prediction2D build2DPrediction(File uValuesFile, File vValuesFile) {
		double[] u = parseDoubles(uValuesFile);
		double[] v = parseDoubles(vValuesFile);
		return new Prediction2D(u, v, uDimension, vDimension);
	}

	public Prediction3D build3DPrediction() {
		File[] files = new File(path).listFiles();
		double[] outputs = null;
		Map<Integer, List<double[]>> data = new HashMap<Integer, List<double[]>>();
		for (File file : files) {
			String[] fileName = file.getName().split("\\.");
			String dataType = String.valueOf(fileName[0].charAt(fileName[0]
					.length() - 1));
			if (typeFilter.contains(dataType)) {
				Integer level = Integer.parseInt(fileName[0].substring(0,
						fileName[0].length() - 1));
				List<double[]> temp = null;
				if ((temp = data.get(level)) == null) {
					data.put(
							level,
							new ArrayList<double[]>(Integer
									.parseInt(parameterCount)));
					temp = data.get(level);
				}
				outputs = parseDoubles(file);
				temp.add(outputs);
				data.put(level, temp);
			}
		}
		return new Prediction3D(data, u3Dimension, v3Dimension);
	}

	private double[] parseDoubles(File file) {
		try {
			String content = FileUtils.readFileToString(file);
			return Arrays.stream(content.split("\n"))
					.mapToDouble(Double::valueOf).toArray();
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
