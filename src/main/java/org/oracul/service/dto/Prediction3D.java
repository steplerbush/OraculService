package org.oracul.service.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PREDICTION3D")
public class Prediction3D {
	@Id
	@Column(name = "PREDICTION_ID")
	private Long id;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "prediction3d", cascade={CascadeType.ALL})
	private List<Level> levels;

	private int gridU;
	private int gridV;

	public Prediction3D(List<Level> levels, int gridU, int gridV) {
		this.gridU = gridU;
		this.gridV = gridV;
		this.levels = levels;
	}

	public Prediction3D() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Level> getLevels() {
		return levels;
	}

	public void setLevels(List<Level> levels) {
		this.levels = levels;
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

	@Override
	public String toString() {
		return "Prediction3D [id=" + id + ", levels=" + levels + ", gridU="
				+ gridU + ", gridV=" + gridV + "]";
	}
	

}
