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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mlaroche.smartheater.dao.HeaterDAO;
import com.mlaroche.smartheater.dao.heater.HeaterPAO;
import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterInfo;
import com.mlaroche.smartheater.domain.HeaterModeEnum;
import com.mlaroche.smartheater.domain.ProtocolEnum;
import com.mlaroche.smartheater.domain.WeeklyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar.DailyCalendar;
import com.mlaroche.smartheater.model.HeaterWeeklyCalendar.TimeSlot;

import io.vertigo.commons.daemon.DaemonScheduled;
import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.component.Activeable;
import io.vertigo.dynamo.criteria.Criterions;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.lang.Assertion;

@Transactional
public class HeaterControlServicesImpl implements HeaterControlServices, Activeable {

	private static Logger logger = LogManager.getLogger(HeaterControlServices.class);

	@Inject
	private HeaterDAO heaterDAO;
	@Inject
	private HeaterPAO heaterPAO;

	@Inject
	private List<RemoteHeaterControlerPlugin> remoteHeaterControlerPlugins;

	private final Map<ProtocolEnum, RemoteHeaterControlerPlugin> pluginByProtocol = new HashMap<>();

	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public void start() {
		Assertion.checkNotNull(remoteHeaterControlerPlugins);
		Assertion.checkState(!remoteHeaterControlerPlugins.isEmpty(), "at least one remoteHeaterControlerPlugin is needed");
		//---
		remoteHeaterControlerPlugins.forEach(plugin -> pluginByProtocol.put(plugin.getProtocol(), plugin));

	}

	@Override
	public void stop() {
		// nothing

	}

	@DaemonScheduled(name = "DMN_HEATER_MODE", periodInSeconds = 60 * 2) // every two minutes
	public void mainLoop() {
		final DtList<Heater> heaters = heaterDAO.findAll(Criterions.alwaysTrue(), Integer.MAX_VALUE);
		final LocalDateTime now = LocalDateTime.now();
		final LocalTime nowTime = now.toLocalTime();

		for (final Heater heater : heaters) {
			// only do it for heaters in auto
			if (heater.getAuto()) {
				heater.weeklyCalendar().load();// at max 20 heaters
				final WeeklyCalendar weeklyCalendar = heater.weeklyCalendar().get();
				final HeaterWeeklyCalendar heaterWeeklyCalendar = gson.fromJson(weeklyCalendar.getJsonValue(), HeaterWeeklyCalendar.class);

				final DailyCalendar dailyCalendar = heaterWeeklyCalendar.getDailyCalendars().get(now.getDayOfWeek());
				final HeaterModeEnum heaterMode = dailyCalendar.getTimeSlots()
						.stream()
						.filter(timeSlot -> nowTime.isAfter(timeSlot.getBegin()) && nowTime.isBefore(timeSlot.getEnd())) //we find the current time slot if any
						.findAny()
						.map(TimeSlot::getMode)
						.orElse(HeaterModeEnum.arret);

				try {
					pluginByProtocol.get(heater.protocol().getEnumValue()).changeHeaterMode(heater, heaterMode);
					heater.mode().setEnumValue(heaterMode);
					heaterDAO.save(heater);
				} catch (final Exception e) {
					logger.error("Unable to change mode of heater : '" + heater.getName() + "'");
				}

			}

		}

	}

	@DaemonScheduled(name = "DMN_HEATER_AUTOSWITCH", periodInSeconds = 60 * 2) // every two minutes
	public void switchToAutoMode() {

	}

	@Override
	public void forceHeaterMode(final Long heaId, final HeaterModeEnum heaterMode) {
		Assertion.checkNotNull(heaId);
		Assertion.checkNotNull(heaterMode);
		//---
		final Heater heater = heaterDAO.get(heaId);
		pluginByProtocol.get(heater.protocol().getEnumValue()).changeHeaterMode(heater, heaterMode);
		heater.mode().setEnumValue(heaterMode);
		heater.setAuto(false);
		heaterDAO.save(heater);
	}

	@Override
	public void switchToAuto(final Long heaId) {
		Assertion.checkNotNull(heaId);
		//---
		heaterPAO.switchHeaterToAuto(heaId);

	}

	@Override
	public HeaterInfo getInfo(final Heater heater) {
		return pluginByProtocol.get(heater.protocol().getEnumValue()).getInfo(heater);
	}

}
