package org.oracul.service.dto;

import java.util.List;
import java.util.Map;


public class Prediction3D {
	private final Map<Integer, List<double[]>> data;
	private final int gridU;
	private final int gridV;

	public Prediction3D(Map<Integer, List<double[]>> data, int gridU, int gridV) {
		this.data = data;
		this.gridU = gridU;
		this.gridV = gridV;
	}

	public Map<Integer, List<double[]>> getData() {
		return data;
	}

	public int getGridU() {
		return gridU;
	}

	public int getGridV() {
		return gridV;
	}
}
