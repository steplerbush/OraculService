package org.oracul.service.controller;

import java.util.Arrays;

import org.oracul.service.dto.Prediction2D;
import org.oracul.service.service.Prediction2DService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MapController {

	@Autowired
	private Prediction2DService prediction2dRepository;

	@RequestMapping(value = "/map/prediction/{id}", method = RequestMethod.GET)
	public String map(Model model, @PathVariable("id") Long id) throws JsonProcessingException {
		Prediction2D p = prediction2dRepository.findById(id);
		ObjectMapper mapper = new ObjectMapper();
		String jsonU = mapper.writeValueAsString(p.getU());

		String jsonV = mapper.writeValueAsString(p.getV());

		model.addAttribute("u", jsonU); 
		model.addAttribute("v", jsonV);
		return "map";
	}
}

