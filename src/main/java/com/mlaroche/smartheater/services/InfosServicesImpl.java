package com.mlaroche.smartheater.services;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterInfo;
import com.mlaroche.smartheater.domain.WeatherInfo;

import io.vertigo.commons.daemon.DaemonScheduled;
import io.vertigo.core.param.ParamManager;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;

public class InfosServicesImpl implements InfosServices {

	private final static Logger LOGGER = LogManager.getLogger(InfosServicesImpl.class);

	private final InfluxDB influxDB;

	@Inject
	private HeaterServices heaterServices;
	@Inject
	private HeaterControlServices heaterControlServices;
	@Inject
	private WeatherServices weatherServices;

	@Inject
	public InfosServicesImpl(
			final ParamManager paramManager) {
		Assertion.checkNotNull(paramManager);
		// ---
		final String serverUrl = paramManager.getParam("influxdb_url").getValueAsString();
		final String dbName = paramManager.getParam("influxdb_dbname").getValueAsString();
		// ---
		influxDB = InfluxDBFactory.connect(serverUrl);

		final boolean dbExist = influxDB.query(new Query("SHOW DATABASES", null))
				.getResults()
				.get(0)
				.getSeries()
				.get(0)
				.getValues()
				.stream()
				.anyMatch(value -> dbName.equals(value));
		if (!dbExist) {
			influxDB.query(new Query("CREATE DATABASE " + dbName, dbName));
		}
		influxDB.setDatabase(dbName);
	}

	@DaemonScheduled(name = "DMN_STORE_INFOS", periodInSeconds = 60 * 1) // every minute
	public void putInfosToInflux() {
		influxDB.enableBatch();
		for (final Heater heater : heaterServices.listHeaters()) {
			try {
				final HeaterInfo heaterInfo = heaterControlServices.getInfo(heater);
				final Point infoPoint = Point.measurement("heater")
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
						.addField("temperature", heaterInfo.getTemperature())
						.addField("humidity", heaterInfo.getHumidity())
						.addField("mode", heaterInfo.getMode())
						.addField("name", heater.getName())
						.addField("dns", heater.getDnsName())
						.tag("name", heater.getName())
						.tag("mode", heaterInfo.getMode())
						.build();
				influxDB.write(infoPoint);
			} catch (final WrappedException e) {
				LOGGER.error(e.getCause());
			}
		}
		final WeatherInfo weatherInfo = weatherServices.getWeatherInfo();
		final Point weatherInfoPoint = Point.measurement("weather")
				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.addField("temperature", weatherInfo.getTemperature())
				.addField("humidity", weatherInfo.getHumidity())
				.addField("description", weatherInfo.getDescription())
				.addField("location", weatherInfo.getLocation())
				.addField("icon", weatherInfo.getIcon())
				.tag("description", weatherInfo.getDescription())
				.tag("location", weatherInfo.getLocation())
				.tag("icon", weatherInfo.getIcon())
				.build();
		influxDB.write(weatherInfoPoint);
		influxDB.disableBatch();

	}

	@Override
	public List<HeaterInfo> getInfos() {
		return Collections.emptyList();
	}

}
