package com.mlaroche.smartheater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;

@Controller
@RequestMapping("")
public class LegacyIndexController extends AbstractVSpringMvcController {

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		// nothing
	}

}
