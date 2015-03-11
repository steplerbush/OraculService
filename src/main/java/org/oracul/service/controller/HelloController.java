package org.oracul.service.controller;

import org.oracul.service.dto.Greetings;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public Greetings printWelcome() {
		return new Greetings("hi from Oracul v 1.2");
	}
}