package com.mlaroche.smartheater.controllers.heater;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterMode;
import com.mlaroche.smartheater.services.HeaterServices;

import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;

@Controller
@RequestMapping("/heaters")
public class HeaterListController extends AbstractVSpringMvcController {

	public final ViewContextKey<Heater> heatersKey = ViewContextKey.of("heaters");
	public final ViewContextKey<HeaterMode> heaterModesKey = ViewContextKey.of("heaterModes");

	@Inject
	private HeaterServices heaterServices;

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		viewContext.publishMdl(heaterModesKey, HeaterMode.class, null); //all
		//---
		viewContext.publishDtList(heatersKey, heaterServices.listHeaters());
		toModeReadOnly();
	}

}
