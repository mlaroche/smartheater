package com.mlaroche.smartheater.services;

import com.mlaroche.smartheater.domain.WeeklyCalendar;

import io.vertigo.core.component.Component;

public interface WeeklyCalendarServices extends Component {

	void saveWeeklyCalendar(WeeklyCalendar weeklyCalendar);

	WeeklyCalendar getWeeklyCalendar(Long wcaId);

}
