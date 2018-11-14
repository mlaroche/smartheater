package com.mlaroche.smartheater.services;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.model.HeaterInfo;

import io.vertigo.commons.daemon.DaemonScheduled;
import io.vertigo.core.param.ParamManager;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;

public class HeaterInfosServicesImpl implements HeaterInfosServices {

	private final InfluxDB influxDB;

	@Inject
	private HeaterServices heaterServices;
	@Inject
	private HeaterControlServices heaterControlServices;

	@Inject
	public HeaterInfosServicesImpl(
			final ParamManager paramManager) {
		Assertion.checkNotNull(paramManager);
		// ---
		final String serverUrl = paramManager.getParam("influxdb_url").getValueAsString();
		// ---
		influxDB = InfluxDBFactory.connect(serverUrl);
		final String dbName = "smartheater";

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

	@DaemonScheduled(name = "DMN_HEATER_INFO", periodInSeconds = 60 * 1) // every minute
	public void putInfosToInflux() {
		influxDB.enableBatch();
		for (final Heater heater : heaterServices.listHeaters()) {
			try {
				final HeaterInfo heaterInfo = heaterControlServices.getInfo(heater);
				final Point infoPoint = Point.measurement("heater")
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
						.addField("temperature", heaterInfo.temperature)
						.addField("humidity", heaterInfo.humidity)
						.addField("mode", heaterInfo.mode)
						.addField("name", heater.getName())
						.addField("dns", heater.getDnsName())
						.tag("name", heater.getName())
						.tag("mode", heaterInfo.mode)
						.build();
				influxDB.write(infoPoint);
			} catch (final WrappedException e) {
				// do nothing if no info
			}
		}
		influxDB.disableBatch();

	}

	@Override
	public List<HeaterInfo> getInfos() {
		return Collections.emptyList();
	}

}
