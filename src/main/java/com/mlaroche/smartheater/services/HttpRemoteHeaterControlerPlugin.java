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

import com.google.gson.Gson;
import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterInfo;
import com.mlaroche.smartheater.domain.HeaterModeEnum;
import com.mlaroche.smartheater.domain.ProtocolEnum;

import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;

public class HttpRemoteHeaterControlerPlugin implements RemoteHeaterControlerPlugin {

	private static final Gson gson = new Gson();

	@Override
	public void changeHeaterMode(final Heater heater, final HeaterModeEnum heaterMode) {
		Assertion.checkNotNull(heater);
		Assertion.checkNotNull(heaterMode);
		//---
		final String url = "http://" + heater.getDnsName() + "/mode/" + heaterMode.name();
		callRestWS(url);

	}

	@Override
	public HeaterInfo getInfo(final Heater heater) {
		Assertion.checkNotNull(heater);
		//---
		final String url = "http://" + heater.getDnsName() + "/infos/";
		return callRestWS(url, HeaterInfo.class);

	}

	@Override
	public ProtocolEnum getProtocol() {
		return ProtocolEnum.http;
	}

	private static void callRestWS(final String wsUrl) {
		Assertion.checkArgNotEmpty(wsUrl);
		// ---
		try {
			final URL url = new URL(wsUrl);
			final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(500);
			httpURLConnection.setReadTimeout(500);
			//---
			Assertion.checkState(httpURLConnection.getResponseCode() == 200, "Error calling '{0}' ", wsUrl);
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}

	}

	private static <R> R callRestWS(final String wsUrl, final Type returnType) {
		Assertion.checkArgNotEmpty(wsUrl);
		// ---
		try {
			final URL url = new URL(wsUrl);
			final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(500);
			httpURLConnection.setReadTimeout(500);
			httpURLConnection.setRequestProperty("Content-Type", "application/json");

			final ByteArrayOutputStream result = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1024];
			try (InputStream inputStream = httpURLConnection.getInputStream()) {
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
					result.write(buffer, 0, length);
				}
			}
			return gson.fromJson(result.toString("UTF-8"), returnType);
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}

	}

}
