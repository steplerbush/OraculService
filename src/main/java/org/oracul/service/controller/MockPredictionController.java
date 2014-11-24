package org.oracul.service.controller;

import org.oracul.service.builder.MockPredictionBuilder;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class MockPredictionController {
	@Autowired
	private MockPredictionBuilder builder;

	@Autowired
	private ResourceLoader resourceLoader;

	@RequestMapping(value = "/mock", method = RequestMethod.GET)
	public Prediction2D getMockPrediction() {

		File uValuesFile;
		File vValuesFile;
		try {
			uValuesFile = resourceLoader.getResource("classpath:mockData/u.out").getFile();
			vValuesFile = resourceLoader.getResource("classpath:mockData/u.out").getFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return builder.build2DPrediction(uValuesFile, vValuesFile);
	}
}
