package org.oracul.service.dto;

public class Prediction2D {
	private final double[] u;
	private final double[] v;

	private final int gridU;
	private final int gridV;

	public Prediction2D(double[] u, double[] v, int gridU, int gridV) {
		this.gridU = gridU;
		this.gridV = gridV;
		this.u = u;
		this.v = v;
	}

	public int getGridU() {
		return gridU;
	}

	public int getGridV() {
		return gridV;
	}

	public double[] getU() {
		return u;
	}

	public double[] getV() {
		return v;
	}
}
