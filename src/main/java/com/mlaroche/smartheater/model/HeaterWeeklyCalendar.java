package com.mlaroche.smartheater.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertigo.lang.Assertion;

public class HeaterWeeklyCalendar {

	private final Map<DayOfWeek, DailyCalendar> dailyCalendars = new HashMap<>();

	public Map<DayOfWeek, DailyCalendar> getDailyCalendars() {
		return dailyCalendars;
	}

	public static class TimeSlot {
		private LocalTime begin;
		private LocalTime end;
		private HeaterMode mode;

		public TimeSlot() {
			//
		}

		public TimeSlot(final LocalTime begin, final LocalTime end, final HeaterMode mode) {
			Assertion.checkNotNull(begin);
			Assertion.checkNotNull(end);
			Assertion.checkNotNull(mode);
			//---
			this.begin = begin;
			this.end = end;
			this.mode = mode;
		}

		public LocalTime getBegin() {
			return begin;
		}

		public void setBegin(final LocalTime begin) {
			this.begin = begin;
		}

		public LocalTime getEnd() {
			return end;
		}

		public void setEnd(final LocalTime end) {
			this.end = end;
		}

		public HeaterMode getMode() {
			return mode;
		}

		public void setMode(final HeaterMode mode) {
			this.mode = mode;
		}

	}

	public static class DailyCalendar {
		private final List<TimeSlot> timeSlots = new ArrayList<>();

		public List<TimeSlot> getTimeSlots() {
			return timeSlots;
		}

	}

}
