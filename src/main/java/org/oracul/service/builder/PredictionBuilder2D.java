package org.oracul.service.builder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PredictionBuilder2D extends PredictionBuilder {
	private static final Logger LOGGER = Logger.getLogger(PredictionBuilder2D.class);

	@Value("${oracul.singleton-results.u}")
	private String singletonUpath;

	@Value("${oracul.singleton-results.v}")
	private String singletonVpath;

	@Autowired
	public PredictionBuilder2D(@Value("${2d.size.u}") Integer uDimension, @Value("${2d.size.v}") Integer vDimension) {
		super(uDimension, vDimension);
	}

	public Prediction2D buildPrediction(Object prediction) {
		Prediction2D prediction2d = (Prediction2D) prediction;
		Long id = prediction2d.getId();
		LOGGER.debug("Prediction2D task#" + id + " files parsing.");
		File uValuesFile = new File(singletonUpath + "/" + id + "/" + "u.out");
		File vValuesFile = new File(singletonVpath + "/" + id + "/" + "v.out");
		double[] u = parseDoubles(uValuesFile);
		double[] v = parseDoubles(vValuesFile);
		prediction2d.setGridU(getuDimension());
		prediction2d.setGridV(getvDimension());
		prediction2d.setU(u);
		prediction2d.setV(v);
		try {
			FileUtils.deleteDirectory(uValuesFile.getParentFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prediction2d;
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
