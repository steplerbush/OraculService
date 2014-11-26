package org.oracul.service;

import org.junit.Assert;
import org.junit.Test;
import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.dto.Prediction2D;

import java.io.File;

public class PredictionBuilderTest {

	public static final String pathToResultFiles = "src/test/resources/twoDimensional/";
	public static final String uFilePath = pathToResultFiles + "u.out";
	public static final String vFilePath = pathToResultFiles + "v.out";

	@Test
	public void shouldBuild2DPrediction() throws Exception {
		PredictionBuilder predictionBuilder = new PredictionBuilder();
		predictionBuilder.setuDimension(60);
		predictionBuilder.setvDimension(61);
		Prediction2D prediction2D = predictionBuilder.build2DPrediction(new File(uFilePath), new File(vFilePath));
		Assert.assertNotNull(prediction2D);
	}
}