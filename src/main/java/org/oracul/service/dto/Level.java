package org.oracul.service.dto;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Level {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "level_id")
	private Long id;

	private Integer lvl;

	@ManyToOne
	@JoinColumn(name = "PREDICTION_ID")
	private Prediction3D prediction3d;

	@Column(name = "u_value")
	private double[] u;

	@Column(name = "v_value")
	private double[] v;

	@Column(name = "t_value")
	private double[] t;

	public Level() {
	}

	public Integer getLvl() {
		return lvl;
	}

	public void setLvl(Integer lvl) {
		this.lvl = lvl;
	}

	public double[] getV() {
		return v;
	}

	public void setV(double[] v) {
		this.v = v;
	}

	public double[] getT() {
		return t;
	}

	public void setT(double[] t) {
		this.t = t;
	}

	public double[] getU() {
		return u;
	}

	public void setU(double[] u) {
		this.u = u;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Prediction3D getPrediction3d() {
		return prediction3d;
	}

	public void setPrediction3d(Prediction3D prediction3d) {
		this.prediction3d = prediction3d;
	}
}
