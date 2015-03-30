package org.oracul.service.builder;

import java.io.File;

import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionBuilder2D extends PredictionBuilder {

	@Value("${oracul.singleton-results.u}")
	private String singletonUpath;

	@Value("${oracul.singleton-results.v}")
	private String singletonVpath;

	@Autowired
	public PredictionBuilder2D(@Value("${2d.size.u}") Integer uDimension, @Value("${2d.size.v}") Integer vDimension) {
		super(uDimension, vDimension);
	}

	public Prediction2D buildPrediction(Long id) {
		File uValuesFile = new File(singletonUpath);
		File vValuesFile = new File(singletonVpath);
		double[] u = parseDoubles(uValuesFile);
		double[] v = parseDoubles(vValuesFile);
		return new Prediction2D(u, v, getuDimension(), getvDimension());
	}

	public String getSingletonUpath() {
		return singletonUpath;
	}

	public void setSingletonUpath(String singletonUpath) {
		this.singletonUpath = singletonUpath;
	}

	public String getSingletonVpath() {
		return singletonVpath;
	}

	public void setSingletonVpath(String singletonVpath) {
		this.singletonVpath = singletonVpath;
	}

}
