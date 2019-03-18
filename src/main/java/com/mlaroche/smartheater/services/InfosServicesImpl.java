package com.mlaroche.smartheater.services;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterInfo;
import com.mlaroche.smartheater.domain.WeatherInfo;

import io.vertigo.commons.daemon.DaemonScheduled;
import io.vertigo.core.param.ParamManager;
import io.vertigo.database.timeseries.Measure;
import io.vertigo.database.timeseries.TimeSeriesDataBaseManager;
import io.vertigo.lang.WrappedException;

public class InfosServicesImpl implements InfosServices {

	private final static Logger LOGGER = LogManager.getLogger(InfosServicesImpl.class);

	@Inject
	private TimeSeriesDataBaseManager timeSeriesDataBaseManager;
	@Inject
	private ParamManager paramManager;

	@Inject
	private HeaterServices heaterServices;
	@Inject
	private HeaterControlServices heaterControlServices;
	@Inject
	private WeatherServices weatherServices;

	@DaemonScheduled(name = "DMN_STORE_INFOS", periodInSeconds = 60 * 1) // every minute
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
				timeSeriesDataBaseManager.insertMeasure(dbName, infoMeasure);
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
		timeSeriesDataBaseManager.insertMeasure(dbName, weatherInfoMeasure);

	}

	@Override
	public List<HeaterInfo> getInfos() {
		return Collections.emptyList();
	}

}
