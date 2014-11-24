package org.oracul.service.controller;

import org.oracul.service.builder.MockPredictionBuilder;
import org.oracul.service.dto.Prediction2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockPredictionController {
	@Autowired
	private MockPredictionBuilder builder;

	@RequestMapping(value = "/mock", method = RequestMethod.GET)
	public Prediction2D getMockPrediction() {
		return builder.build2DPrediction();
	}
}
