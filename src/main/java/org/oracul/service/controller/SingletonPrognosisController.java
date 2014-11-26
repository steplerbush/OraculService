package org.oracul.service.controller;

import org.oracul.service.dto.Prediction2D;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingletonPrognosisController {
	@RequestMapping(value = "/singleton", method = RequestMethod.GET)
	public Prediction2D getMockPrediction() {
		return null;
	}
}
