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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mlaroche.smartheater.model.WeatherInfo;

import io.vertigo.commons.daemon.DaemonScheduled;
import io.vertigo.core.component.Activeable;
import io.vertigo.core.param.ParamManager;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;

public class WeatherServicesImpl implements WeatherServices, Activeable {

	@Inject
	private ParamManager paramManager;

	private WeatherInfo weatherInfo;

	private static final String API_KEY_PARAM_NAME = "openweather_api_key";
	private static final Gson GSON = new Gson();

	@Override
	public void start() {
		retrieveData();

	}

	@Override
	public void stop() {
		// nothing

	}

	@DaemonScheduled(name = "DMN_WEATHER_UPDATE", periodInSeconds = 60 * 1) // every minutes
	public void updateWeather() {
		retrieveData();
	}

	private void retrieveData() {
		final String apiKey = paramManager.getParam(API_KEY_PARAM_NAME).getValueAsString();
		final JsonObject result = callRestWS("http://api.openweathermap.org/data/2.5/weather?id=3016078&lang=fr&units=metric&APPID=" + apiKey, JsonObject.class);
		final WeatherInfo newWeatherInfo = new WeatherInfo();
		newWeatherInfo.setTemperature(result.getAsJsonObject("main").get("temp").getAsDouble());
		newWeatherInfo.setHumidity(result.getAsJsonObject("main").get("humidity").getAsDouble());
		newWeatherInfo.setLocation(result.get("name").getAsString());
		newWeatherInfo.setDescription(result.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString());
		newWeatherInfo.setIcon(result.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString());
		weatherInfo = newWeatherInfo;
	}

	@Override
	public WeatherInfo getWeatherInfo() {
		return weatherInfo;
	}

	private static <R> R callRestWS(final String wsUrl, final Type returnType) {
		Assertion.checkArgNotEmpty(wsUrl);
		// ---
		try {
			final URL url = new URL(wsUrl);
			final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(500);
			httpURLConnection.setRequestProperty("Content-Type", "application/json");

			final ByteArrayOutputStream result = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1024];
			try (InputStream inputStream = httpURLConnection.getInputStream()) {
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
					result.write(buffer, 0, length);
				}
			}
			return GSON.fromJson(result.toString("UTF-8"), returnType);
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}

	}

}
