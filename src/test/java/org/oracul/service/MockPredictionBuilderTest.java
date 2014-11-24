package org.oracul.service;

import org.junit.Assert;
import org.junit.Test;
import org.oracul.service.builder.MockPredictionBuilder;
import org.oracul.service.dto.Prediction2D;

import java.io.File;

public class MockPredictionBuilderTest {

	public static final String pathToResultFiles = "src/test/resources/twoDimensional/";
	public static final String uFilePath = pathToResultFiles + "u.out";
	public static final String vFilePath = pathToResultFiles + "v.out";

	@Test
	public void shouldBuild2DPrediction() throws Exception {
		MockPredictionBuilder mockPredictionBuilder = new MockPredictionBuilder();
		mockPredictionBuilder.setuDimension(60);
		mockPredictionBuilder.setvDimension(61);
		Prediction2D prediction2D = mockPredictionBuilder.build2DPrediction(new File(uFilePath), new File(vFilePath));
		Assert.assertNotNull(prediction2D);
	}
}