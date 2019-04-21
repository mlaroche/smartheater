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
package com.mlaroche.smartheater.jobs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.mlaroche.smartheater.domain.ElectricalConsumption;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.core.param.ParamManager;
import io.vertigo.database.timeseries.DataFilter;
import io.vertigo.database.timeseries.Measure;
import io.vertigo.database.timeseries.TimeFilter;
import io.vertigo.database.timeseries.TimeSeriesDataBaseManager;
import io.vertigo.database.timeseries.TimedDatas;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;
import io.vertigo.orchestra.services.execution.RunnableActivityEngine;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EnedisElectricalConsumptionActivityEngine extends RunnableActivityEngine {

	@Inject
	private ParamManager paramManager;
	@Inject
	private CodecManager codecManager;
	@Inject
	private TimeSeriesDataBaseManager timeSeriesDataBaseManager;

	private static final String ENEDIS_LOGIN_PARAM = "enedis_login";
	private static final String ENEDIS_PASSWORD_PARAM = "enedis_password";
	private static final DateTimeFormatter ENEDIS_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final Gson GSON = new Gson();

	@Override
	public void run() {

		final String dbName = paramManager.getParam("influxdb_dbname").getValueAsString();
		final TimedDatas lastConsumption = timeSeriesDataBaseManager.getTabularTimedData(dbName, Arrays.asList("meanPower:last"), DataFilter.builder("electricalConsumption").build(), TimeFilter.builder("now() - 30w", "now()").build());
		final LocalDate from;
		if (!lastConsumption.getTimedDataSeries().isEmpty()) {
			from = lastConsumption.getTimedDataSeries().get(0).getTime().atZone(ZoneId.of("Europe/Paris")).toLocalDate();
		} else {
			from = LocalDate.now().minusMonths(6); // go back 6 months just to have some data
		}

		if (from.until(LocalDate.now()).getDays() > 1) {
			//there is data to be retrieve
			final List<ElectricalConsumption> electricalConsumptions = retrieveHourData(from, LocalDate.now());
			final List<Measure> measures = electricalConsumptions.stream().map(electricalConsumption -> Measure.builder("electricalConsumption")
					.time(electricalConsumption.getTimestamp())
					.addField("meanPower", electricalConsumption.getMeanPower())
					.build()).collect(Collectors.toList());
			timeSeriesDataBaseManager.insertMeasures(dbName, measures);
		}
	}

	private List<ElectricalConsumption> retrieveHourData(final LocalDate from, final LocalDate to) {

		final CookieJar cookieJar = new CookieJar() {
			private final List<Cookie> cookies = new ArrayList<>();

			@Override
			public void saveFromResponse(final HttpUrl url, final List<Cookie> cookies) {
				this.cookies.addAll(cookies);
			}

			@Override
			public List<Cookie> loadForRequest(final HttpUrl url) {
				return cookies;

			}
		};

		final OkHttpClient client = new OkHttpClient.Builder()
				.followRedirects(false)
				.cookieJar(cookieJar)
				.build();

		final String login = paramManager.getParam(ENEDIS_LOGIN_PARAM).getValueAsString();
		final String password = paramManager.getParam(ENEDIS_PASSWORD_PARAM).getValueAsString();

		final RequestBody formBodyLogin = new FormBody.Builder()
				.add("IDToken1", login)
				.add("IDToken2", password)
				.add("goto", codecManager.getBase64Codec().encode("https://espace-client-particuliers.enedis.fr/group/espace-particuliers/accueil".getBytes(StandardCharsets.UTF_8)))
				.add("SunQueryParamsString", codecManager.getBase64Codec().encode("realm=particuliers".getBytes(StandardCharsets.UTF_8)))
				.add("encoded", "true")
				.add("gx_charset", "UTF-8")
				.build();

		final Request requestLogin = new Request.Builder()
				.url("https://espace-client-connexion.enedis.fr/auth/UI/Login")
				.post(formBodyLogin)
				.build();

		final RequestBody formBodyData = new FormBody.Builder()
				.add("p_p_id", "lincspartdisplaycdc_WAR_lincspartcdcportlet")
				.add("p_p_lifecycle", "2")
				.add("p_p_resource_id", "urlCdcHeure")
				.add("_lincspartdisplaycdc_WAR_lincspartcdcportlet_dateDebut", from.format(ENEDIS_DATE_FORMAT))
				.add("_lincspartdisplaycdc_WAR_lincspartcdcportlet_dateFin", to.format(ENEDIS_DATE_FORMAT))
				.build();

		final Request requestData = new Request.Builder()
				.url("https://espace-client-particuliers.enedis.fr/group/espace-particuliers/suivi-de-consommation")
				.post(formBodyData)
				.build();

		final EnedisInfo enedisInfo;
		try {
			client.newCall(requestLogin).execute().close();//login
			client.newCall(requestData).execute().close(); //first call needed don't know why maybe a cookie...

			final Response response = client.newCall(requestData).execute();
			enedisInfo = GSON.fromJson(response.body().string(), EnedisInfo.class);
			response.close();
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}

		Assertion.checkState("termine".equals(enedisInfo.etat.valeur), "Error retrieving infos from Enedis");
		final Instant debut = LocalDate.parse(enedisInfo.graphe.periode.dateDebut, ENEDIS_DATE_FORMAT).atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
		return enedisInfo.graphe.data
				.stream()
				.filter(enedisData -> enedisData.valeur != -2.0)
				.map(enedisData -> {
					final ElectricalConsumption electricalConsumption = new ElectricalConsumption();
					electricalConsumption.setTimestamp(debut.plus(30 * (enedisData.ordre - 1), ChronoUnit.MINUTES));
					electricalConsumption.setMeanPower(enedisData.valeur);
					return electricalConsumption;
				}).collect(Collectors.toList());

	}

	private static class EnedisInfo {
		public EtatEnedis etat;
		public GrapheEnedis graphe;

		private static class EtatEnedis {
			public String valeur;
		}

		private static class GrapheEnedis {
			public Integer puissanceSouscrite;
			public Integer decalage;
			public PeriodeEnedis periode;
			public List<DataEnedis> data = new ArrayList<>();
		}

		private static class PeriodeEnedis {
			public String dateDebut;
			public String dateFin;
		}

		private static class DataEnedis {
			public Double valeur;
			public Integer ordre;
		}

	}

}
