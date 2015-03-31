package org.oracul.service.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.oracul.service.dto.Prediction3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionBuilder3D extends PredictionBuilder {

	@Value("${oracul.root.output.3d}")
	private String pathToFiles;

	@Value("${type.filter}")
	private String typeFilter;

	@Autowired
	public PredictionBuilder3D(@Value("${3d.size.u}") Integer u3Dimension, @Value("${3d.size.v}") Integer v3Dimension) {
		super(u3Dimension, v3Dimension);
	}

	public Prediction3D buildPrediction(Long id) {
		File[] files = new File(pathToFiles + "/" + id + "/").listFiles();
		List<Double> outputs = new ArrayList<Double>();
		Map<Integer, List<List<Double>>> data = new HashMap<Integer, List<List<Double>>>();
		for (File file : files) {
			String[] fileName = file.getName().split("\\.");
			String dataType = String.valueOf(fileName[0].charAt(fileName[0].length() - 1));
			if (typeFilter.contains(dataType)) {
				Integer level = Integer.parseInt(fileName[0].substring(0, fileName[0].length() - 1));
				List<List<Double>> temp = null;
				if ((temp = data.get(level)) == null) {
					data.put(level, new ArrayList<List<Double>>());
					temp = data.get(level);
				}
				outputs.addAll(Arrays.asList(ArrayUtils.toObject(parseDoubles(file))));
				temp.add(outputs);
				data.put(level, temp);
			}
		}
		Prediction3D prediction3d = new Prediction3D(data, getuDimension(), getvDimension());
		prediction3d.setId(id);
		return prediction3d;
	}

	public String getPath() {
		return pathToFiles;
	}

	public void setTypeFilter(String typeFilter) {
		this.typeFilter = typeFilter;
	}

	public String getPathToFiles() {
		return pathToFiles;
	}

	public void setPathToFiles(String pathToFiles) {
		this.pathToFiles = pathToFiles;
	}

	public String getTypeFilter() {
		return typeFilter;
	}

}
