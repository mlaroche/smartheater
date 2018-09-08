/**
 * Copyright 2018 Matthieu Laroche - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mlaroche.smartheater.services;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mlaroche.smartheater.dao.WeeklyCalendarDAO;
import com.mlaroche.smartheater.domain.WeeklyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.VUserException;

@Transactional
public class WeeklyCalendarServicesImpl implements WeeklyCalendarServices {

	@Inject
	private WeeklyCalendarDAO weeklyCalendarDAO;

	private final Gson gson = new Gson();

	@Override
	public void saveWeeklyCalendar(final WeeklyCalendar weeklyCalendar) {
		Assertion.checkNotNull(weeklyCalendar);
		//---
		// just to make sure that the weeklyCalendar is valid we marshall and unmarshall the json value
		try {
			gson.toJson(gson.fromJson(weeklyCalendar.getJsonValue(), HeaterWeeklyCalendar.class));
		} catch (final JsonParseException e) {
			throw new VUserException("json calendar is not valid : {0}", weeklyCalendar.getJsonValue());
		}
		weeklyCalendarDAO.save(weeklyCalendar);
	}

	@Override
	public WeeklyCalendar getWeeklyCalendar(final Long wcaId) {
		Assertion.checkNotNull(wcaId);
		//---
		return weeklyCalendarDAO.get(wcaId);

	}

}
