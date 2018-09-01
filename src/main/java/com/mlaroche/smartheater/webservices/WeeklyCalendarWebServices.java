package com.mlaroche.smartheater.webservices;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.inject.Inject;

import com.mlaroche.smartheater.domain.WeeklyCalendar;
import com.mlaroche.smartheater.model.HeaterMode;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar.DailyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar.TimeSlot;
import com.mlaroche.smartheater.services.WeeklyCalendarServices;

import io.vertigo.lang.Assertion;
import io.vertigo.vega.webservice.WebServices;
import io.vertigo.vega.webservice.stereotype.GET;
import io.vertigo.vega.webservice.stereotype.POST;
import io.vertigo.vega.webservice.stereotype.PUT;
import io.vertigo.vega.webservice.stereotype.PathParam;
import io.vertigo.vega.webservice.stereotype.PathPrefix;

@PathPrefix("/weeklyCalendars")
public class WeeklyCalendarWebServices implements WebServices {

	@Inject
	private WeeklyCalendarServices weeklyCalendarServices;

	@GET("/test")
	public HeaterWeeklyCalendar test() {
		final DailyCalendar defaultCalendar = new DailyCalendar();
		defaultCalendar.getTimeSlots()
				.add(new TimeSlot(LocalTime.of(7, 0), LocalTime.of(8, 0), HeaterMode.confort));

		final HeaterWeeklyCalendar heaterWeeklyCalendar = new HeaterWeeklyCalendar();
		heaterWeeklyCalendar.getDailyCalendars()
				.put(DayOfWeek.MONDAY, defaultCalendar);

		return heaterWeeklyCalendar;

	}

	@POST("")
	public void createCalendar(final WeeklyCalendar weeklyCalendar) {
		weeklyCalendarServices.saveWeeklyCalendar(weeklyCalendar);

	}

	@PUT("/{wcaId}")
	public void save(@PathParam("wcaId") final Long wcaId, final WeeklyCalendar weeklyCalendar) {
		Assertion.checkState(wcaId.equals(weeklyCalendar.getWcaId()), "you are trying to update the wrong weelycalendar");
		//---
		weeklyCalendarServices.saveWeeklyCalendar(weeklyCalendar);

	}

	@GET("/{wcaId}")
	public WeeklyCalendar get(@PathParam("wcaId") final Long wcaId) {
		return weeklyCalendarServices.getWeeklyCalendar(wcaId);

	}

}
