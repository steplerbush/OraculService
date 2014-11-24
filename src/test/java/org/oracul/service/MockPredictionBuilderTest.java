package org.oracul.service;

import org.junit.Assert;
import org.junit.Test;
import org.oracul.service.builder.MockPredictionBuilder;
import org.oracul.service.dto.Prediction2D;

public class MockPredictionBuilderTest {

	public static final String pathToResultFiles = "src/test/resources/twoDimensional/";
	public static final String uFile = pathToResultFiles + "u.out";
	public static final String vFile = pathToResultFiles + "v.out";

	@Test
	public void shouldBuild2DPrediction() throws Exception {
		MockPredictionBuilder mockPredictionBuilder = new MockPredictionBuilder();
		mockPredictionBuilder.setuDimension(60);
		mockPredictionBuilder.setvDimension(61);
//		mockPredictionBuilder.setuValuesPath(uFile);
//		mockPredictionBuilder.setvValuesPath(vFile);
		Prediction2D prediction2D = mockPredictionBuilder.build2DPrediction();
		Assert.assertNotNull(prediction2D);
	}
}