package com.mlaroche.smartheater.controllers.calendar;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mlaroche.smartheater.domain.WeeklyCalendar;
import com.mlaroche.smartheater.services.WeeklyCalendarServices;

import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;

@Controller
@RequestMapping("/calendars")
public class CalendarListController extends AbstractVSpringMvcController {

	public final ViewContextKey<WeeklyCalendar> calendarsKey = ViewContextKey.of("calendars");

	@Inject
	private WeeklyCalendarServices weeklyCalendarServices;

	@GetMapping("/")
	public void initContext(final ViewContext viewContext) {
		viewContext.publishDtList(calendarsKey, weeklyCalendarServices.listCalendars());
		toModeReadOnly();
	}

}
