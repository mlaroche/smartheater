package com.mlaroche.smartheater.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mlaroche.smartheater.domain.DtDefinitions.HeatersByModeFields;
import com.mlaroche.smartheater.services.HeaterServices;
import com.mlaroche.smartheater.services.WeatherServices;

import io.vertigo.core.param.ParamManager;
import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;

@Controller
@RequestMapping("/home")
public class HomeController extends AbstractVSpringMvcController {

	@Inject
	private WeatherServices weatherServices;
	@Inject
	private HeaterServices heaterServices;
	@Inject
	private ParamManager paramManager;

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		viewContext.publishDto(() -> "weatherInfo", weatherServices.getWeatherInfo());
		viewContext.publishDtList(() -> "countsByMode", HeatersByModeFields.mode, heaterServices.getHeatersByMode());
		viewContext.publishRef(() -> "chronografUrl", paramManager.getParam("chronograf_url").getValueAsString());
	}

}
