package org.oracul.service.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Entity
public class Level {
	@Id
	@Column(name = "PREDICTION_ID", nullable = false)
	private Long id;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "level_test", joinColumns = @JoinColumn(name = "test_id"))
	@Column(name = "v_value")
	private List<Double> v = new ArrayList<>();

	// @ElementCollection(fetch = FetchType.EAGER)
	// @JoinTable(name = "level_data", joinColumns = @JoinColumn(name =
	// "level_data_id"))
	// @Basic(fetch=FetchType.EAGER)
	// private List<Double> data = new ArrayList<>();
	//
	// public Level() {
	// }
	//
	// public List<Double> getData() {
	// return data;
	// }
	//
	// public void setData(List<Double> data) {
	// this.data = data;
	// }

}
