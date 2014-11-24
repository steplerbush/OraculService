package org.oracul.service.dto;

import org.apache.commons.lang3.Validate;

public class Prediction2D {
	private final double[] u;
	private final double[] v;
	private final int gridU;
	private final int gridV;

	public Prediction2D(double[] u, double[] v, int gridU, int gridV) {
		Validate.notNull(u, "null u");
		Validate.notNull(v, "null v");
		int expectedSize = gridU * gridV;
		Validate.isTrue(expectedSize == v.length, "v is of wrong size:" + v.length
				+ " expected size is :" + expectedSize);
		Validate.isTrue(expectedSize == u.length, "u is of wrong size:" + u.length
				+ " expected size is :" + expectedSize);
		this.u = u;
		this.v = v;
		this.gridU = gridU;
		this.gridV = gridV;
	}

	public double[] getU() {
		return u;
	}

	public double[] getV() {
		return v;
	}

	public int getGridU() {
		return gridU;
	}

	public int getGridV() {
		return gridV;
	}
}
