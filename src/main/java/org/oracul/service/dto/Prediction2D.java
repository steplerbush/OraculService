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
	@Column(name = "PREDICTION_ID", nullable = false)
	private Long id;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "prediction2d_u", joinColumns = @JoinColumn(name = "prediction2d_id"))
	@Column(name = "u_value")
	private List<Double> u = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "prediction2d_v", joinColumns = @JoinColumn(name = "prediction2d_id"))
	@Column(name = "v_value")
	private List<Double> v = new ArrayList<>();

	private int gridU;
	private int gridV;

	public Prediction2D() {
	}

	public Prediction2D(List<Double> u, List<Double> v, int gridU, int gridV) {
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

	public List<Double> getU() {
		return u;
	}

	public List<Double> getV() {
		return v;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
