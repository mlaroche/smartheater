package com.mlaroche.smartheater.services;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterInfo;
import com.mlaroche.smartheater.domain.WeatherInfo;

import io.vertigo.core.daemon.DaemonScheduled;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.param.ParamManager;
import io.vertigo.database.timeseries.DataFilter;
import io.vertigo.database.timeseries.Measure;
import io.vertigo.database.timeseries.TimeFilter;
import io.vertigo.database.timeseries.TimeSeriesManager;
import io.vertigo.database.timeseries.TimedDatas;

public class InfosServicesImpl implements InfosServices {

	private final static Logger LOGGER = LogManager.getLogger(InfosServicesImpl.class);

	@Inject
	private TimeSeriesManager timeSeriesManager;
	@Inject
	private ParamManager paramManager;

	@Inject
	private HeaterServices heaterServices;
	@Inject
	private HeaterControlServices heaterControlServices;
	@Inject
	private WeatherServices weatherServices;

	@DaemonScheduled(name = "DmnStoreInfos", periodInSeconds = 60 * 1) // every minute
	public void putInfosToInflux() {
		final String dbName = paramManager.getParam("influxdb_dbname").getValueAsString();

		for (final Heater heater : heaterServices.listHeaters()) {
			try {
				final HeaterInfo heaterInfo = heaterControlServices.getInfo(heater);
				final Measure infoMeasure = Measure.builder("heater")
						.time(Instant.now())
						.addField("temperature", heaterInfo.getTemperature())
						.addField("humidity", heaterInfo.getHumidity())
						.addField("mode", heaterInfo.getMode())
						.addField("name", heater.getName())
						.addField("dns", heater.getDnsName())
						.tag("name", heater.getName())
						.tag("mode", heaterInfo.getMode())
						.build();
				timeSeriesManager.insertMeasure(dbName, infoMeasure);
			} catch (final WrappedException e) {
				LOGGER.error(e.getCause());
			}
		}
		final WeatherInfo weatherInfo = weatherServices.getWeatherInfo();
		final Measure weatherInfoMeasure = Measure.builder("weather")
				.time(Instant.now())
				.addField("temperature", weatherInfo.getTemperature())
				.addField("humidity", weatherInfo.getHumidity())
				.addField("description", weatherInfo.getDescription())
				.addField("location", weatherInfo.getLocation())
				.addField("icon", weatherInfo.getIcon())
				.tag("description", weatherInfo.getDescription())
				.tag("location", weatherInfo.getLocation())
				.tag("icon", weatherInfo.getIcon())
				.build();
		timeSeriesManager.insertMeasure(dbName, weatherInfoMeasure);

	}

	@Override
	public List<HeaterInfo> getInfos() {
		return Collections.emptyList();
	}

	@Override
	public TimedDatas getWeekElectricalData() {
		final String dbName = paramManager.getParam("influxdb_dbname").getValueAsString();
		return timeSeriesManager.getTimeSeries(dbName, Arrays.asList("meanPower:mean"), DataFilter.builder("electricalConsumption").build(), TimeFilter.builder("now() - 1w", "now()").withTimeDim("30m").build());
	}

	@Override
	public TimedDatas getWeekMeanIndoorTemperature() {
		final String dbName = paramManager.getParam("influxdb_dbname").getValueAsString();
		return timeSeriesManager.getTimeSeries(dbName, Arrays.asList("temperature:mean"), DataFilter.builder("heater").build(), TimeFilter.builder("now() - 1w", "now()").withTimeDim("30m").build());
	}

	@Override
	public TimedDatas getWeekMeanOutdoorTemperature() {
		final String dbName = paramManager.getParam("influxdb_dbname").getValueAsString();
		return timeSeriesManager.getTimeSeries(dbName, Arrays.asList("temperature:mean"), DataFilter.builder("weather").build(), TimeFilter.builder("now() - 1w", "now()").withTimeDim("30m").build());
	}

	@Override
	public TimedDatas getLastDayHeaterInfos(final Heater heater) {
		Assertion.check().isNotNull(heater);
		//---
		final String dbName = paramManager.getParam("influxdb_dbname").getValueAsString();
		return timeSeriesManager.getTimeSeries(dbName, Arrays.asList("temperature:mean", "humidity:mean"), DataFilter.builder("heater").addFilter("name", heater.getName()).build(), TimeFilter.builder("now() - 1d", "now()").withTimeDim("6m").build());
	}

}
