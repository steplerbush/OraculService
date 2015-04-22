package org.oracul.service.controller;

import org.oracul.service.dto.Level;
import org.oracul.service.dto.Prediction2D;
import org.oracul.service.dto.Prediction3D;
import org.oracul.service.service.Prediction2DService;
import org.oracul.service.service.Prediction3DService;
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

	@Autowired
	private Prediction3DService prediction3dRepository;

	@RequestMapping(value = "/map/prediction/2d/{id}", method = RequestMethod.GET)
	public String map2D(Model model, @PathVariable("id") Long id) throws JsonProcessingException {
		Prediction2D p = prediction2dRepository.findById(id);
		if (p != null) {
			ObjectMapper mapper = new ObjectMapper();
			String jsonU = mapper.writeValueAsString(p.getU());

			String jsonV = mapper.writeValueAsString(p.getV());

			model.addAttribute("u", jsonU);
			model.addAttribute("v", jsonV);
		}

		return "map2d";
	}

	@RequestMapping(value = "/map/prediction/3d/{id}", method = RequestMethod.GET)
	public String map3D(Model model, @PathVariable("id") Long id) throws JsonProcessingException {
		Prediction3D p = prediction3dRepository.findById(id);
		if (p != null) {
			ObjectMapper mapper = new ObjectMapper();
			Level level = p.getLevels().get(0);

			String jsonU = mapper.writeValueAsString(level.getU());

			String jsonV = mapper.writeValueAsString(level.getV());

			String jsonT = mapper.writeValueAsString(level.getT());
			model.addAttribute("u", jsonU);
			model.addAttribute("v", jsonV);
			model.addAttribute("t", jsonT);
		}

		return "map3d";
	}
}
