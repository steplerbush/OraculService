package org.oracul.service.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

@Entity
@Table(name = "PREDICTION_2D")
public class Prediction2D {

	@Id
	private Long id;

	@Column(name = "u_value")
	private double[] u;
	@Column(name = "v_value")
	private double[] v;

	private int gridU;
	private int gridV;

	public Prediction2D() {
	}

	public Prediction2D(double[] u, double[] v, int gridU, int gridV) {
		this.gridU = gridU;
		this.gridV = gridV;
		this.u = u;
		this.v = v;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double[] getU() {
		return u;
	}

	public void setU(double[] u) {
		this.u = u;
	}

	public double[] getV() {
		return v;
	}

	public void setV(double[] v) {
		this.v = v;
	}

	public int getGridU() {
		return gridU;
	}

	public void setGridU(int gridU) {
		this.gridU = gridU;
	}

	public int getGridV() {
		return gridV;
	}

	public void setGridV(int gridV) {
		this.gridV = gridV;
	}

}
