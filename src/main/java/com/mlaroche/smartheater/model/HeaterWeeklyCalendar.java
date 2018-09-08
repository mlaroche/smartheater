/**
 * Copyright [2018] [Matthieu Laroche - France]
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
