package com.mlaroche.smartheater.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaterWeeklyCalendar {
	

	private final Map<DayOfWeek, DailyCalendar> dailyCalendars = new HashMap<>();

	public Map<DayOfWeek, DailyCalendar> getDailyCalendars() {
		return dailyCalendars;
	}
	
	
	public class TimeSlot {
		private LocalTime begin;
		private LocalTime end;
		private HeaterMode mode;
		
		
		public LocalTime getBegin() {
			return begin;
		}
		public void setBegin(LocalTime begin) {
			this.begin = begin;
		}
		
		public LocalTime getEnd() {
			return end;
		}
		public void setEnd(LocalTime end) {
			this.end = end;
		}
		
		public HeaterMode getMode() {
			return mode;
		}
		public void setMode(HeaterMode mode) {
			this.mode = mode;
		}
		

	}

	public class DailyCalendar {
		private final List<TimeSlot> timeSlots = new ArrayList<>();

		public List<TimeSlot> getTimeSlots() {
			return timeSlots;
		}


	}


}


