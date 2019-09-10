package com.mlaroche.smartheater.controllers;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mlaroche.smartheater.domain.DtDefinitions.HeatersByModeFields;
import com.mlaroche.smartheater.services.HeaterServices;
import com.mlaroche.smartheater.services.InfosServices;
import com.mlaroche.smartheater.services.WeatherServices;

import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;

@Controller
@RequestMapping("")
public class HomeController extends AbstractVSpringMvcController {

	@Inject
	private WeatherServices weatherServices;
	@Inject
	private HeaterServices heaterServices;
	@Inject
	private InfosServices infosServices;

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		viewContext.publishDto(() -> "weatherInfo", weatherServices.getWeatherInfo());
		viewContext.publishDtList(() -> "countsByMode", HeatersByModeFields.mode, heaterServices.getHeatersByMode());

		viewContext.publishRef(() -> "electricalData", infosServices.getWeekElectricalData());
		viewContext.publishRef(() -> "indoorTemperatureData", infosServices.getWeekMeanIndoorTemperature());
		viewContext.publishRef(() -> "outdoorTemperatureData", infosServices.getWeekMeanOutdoorTemperature());
	}

}
