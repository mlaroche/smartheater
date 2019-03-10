package com.mlaroche.smartheater.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mlaroche.smartheater.domain.DtDefinitions.HeatersByModeFields;
import com.mlaroche.smartheater.services.HeaterServices;
import com.mlaroche.smartheater.services.WeatherServices;

import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;

@Controller
@RequestMapping("/home")
public class HomeController extends AbstractVSpringMvcController {

	@Inject
	private WeatherServices weatherServices;

	@Inject
	private HeaterServices heaterServices;

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		viewContext.asMap().put("weatherInfo", weatherServices.getWeatherInfo());
		viewContext.publishDtList(() -> "countsByMode", HeatersByModeFields.MODE, heaterServices.getHeatersByMode());
	}

}