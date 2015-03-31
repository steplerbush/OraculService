package org.oracul.service.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Table(name = "PREDICTION_3D")
public class Prediction3D {
	@Id
	@Column(name = "PREDICTION_ID", nullable = false)
	private Long id;

	@JoinTable(name = "level_data", joinColumns = @JoinColumn(name = "prediction_3d_id"))
	@Column(name = "data")
	@MapKeyColumn(name = "level")
	private Map<Integer, Level> levelData = new HashMap<>();

	private int gridU;
	private int gridV;

	public Map<Integer, Level> getLevelData() {
		return levelData;
	}

	public Prediction3D(Map<Integer, List<List<Double>>> data, int gridU, int gridV) {
		this.gridU = gridU;
		this.gridV = gridV;
		// this.data = data;
	}

	public Prediction3D() {
	}

	// public Map<Integer, List<List<Double>>> getData() {
	// return data;
	// }

	public int getGridU() {
		return gridU;
	}

	public int getGridV() {
		return gridV;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
