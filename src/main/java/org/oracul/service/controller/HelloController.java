package org.oracul.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome() {
		return new String("Welcome to Oracul Service v 1.2.1");
	}
	
}