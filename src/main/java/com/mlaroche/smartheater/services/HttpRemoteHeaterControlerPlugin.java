package com.mlaroche.smartheater.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.ProtocolEnum;
import com.mlaroche.smartheater.model.HeaterMode;

import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;

public class HttpRemoteHeaterControlerPlugin implements RemoteHeaterControlerPlugin {

	@Override
	public void changeHeaterMode(final Heater heater, final HeaterMode heaterMode) {
		Assertion.checkNotNull(heater);
		Assertion.checkNotNull(heaterMode);
		//---
		final String url = "http://" + heater.getDnsName() + "/mode/" + heaterMode.name();
		callRestWS(url);

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
			//---
			Assertion.checkState(httpURLConnection.getResponseCode() == 200, "Error calling '{0}' ", wsUrl);
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}

	}

}
