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
