package com.mlaroche.smartheater.webservices;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.mlaroche.smartheater.model.HeaterMode;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar.DailyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar.TimeSlot;

import io.vertigo.vega.webservice.WebServices;
import io.vertigo.vega.webservice.stereotype.GET;
import io.vertigo.vega.webservice.stereotype.PathPrefix;

@PathPrefix("/weeklyCalendar")
public class WeeklyCalendarWebServices implements WebServices {

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

}
