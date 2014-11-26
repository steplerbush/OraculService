package org.oracul.service.controller;

import org.oracul.service.builder.PredictionBuilder;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class MockPredictionController {

	@Autowired
	private PredictionBuilder builder;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${mock.u.path}")
	private String mockUpath;

	@Value("${mock.v.path}")
	private String mockVpath;

	@RequestMapping(value = "/mock", method = RequestMethod.GET)
	public Prediction2D getMockPrediction() {

		File uValuesFile;
		File vValuesFile;
		try {
			uValuesFile = resourceLoader.getResource(mockUpath).getFile();
			vValuesFile = resourceLoader.getResource(mockVpath).getFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return builder.build2DPrediction(uValuesFile, vValuesFile);
	}
}
