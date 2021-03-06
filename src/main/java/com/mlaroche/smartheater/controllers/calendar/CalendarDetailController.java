/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mlaroche.smartheater.controllers.calendar;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mlaroche.smartheater.domain.WeeklyCalendar;
import com.mlaroche.smartheater.services.WeeklyCalendarServices;

import io.vertigo.ui.core.ViewContext;
import io.vertigo.ui.core.ViewContextKey;
import io.vertigo.ui.impl.springmvc.argumentresolvers.ViewAttribute;
import io.vertigo.ui.impl.springmvc.controller.AbstractVSpringMvcController;

@Controller
@RequestMapping("/calendar/")
public final class CalendarDetailController extends AbstractVSpringMvcController {

	public final ViewContextKey<WeeklyCalendar> calendarKey = ViewContextKey.of("calendar");

	@Inject
	private WeeklyCalendarServices weeklyCalendarServices;

	@GetMapping("/new")
	public void initContext(final ViewContext viewContext) {
		viewContext.publishDto(calendarKey, new WeeklyCalendar());
		//---
		toModeCreate();
	}

	@GetMapping("/{wcaId}")
	public void initContext(final ViewContext viewContext, @PathVariable("wcaId") final Long wcaId) {
		viewContext.publishDto(calendarKey, weeklyCalendarServices.getWeeklyCalendar(wcaId));
		toModeReadOnly();
	}

	@PostMapping("/_edit")
	public void doEdit() {
		toModeEdit();
	}

	@PostMapping("/_save")
	public String doSave(final ViewContext viewContext, @ViewAttribute("calendar") final WeeklyCalendar weeklyCalendar) {
		weeklyCalendarServices.saveWeeklyCalendar(weeklyCalendar);
		return "redirect:/calendar/" + weeklyCalendar.getWcaId();
	}
}
