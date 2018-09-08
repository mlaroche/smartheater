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
package com.mlaroche.smartheater.webservices;

import javax.inject.Inject;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.model.HeaterMode;
import com.mlaroche.smartheater.services.HeaterControlServices;

import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.vega.webservice.WebServices;
import io.vertigo.vega.webservice.stereotype.GET;
import io.vertigo.vega.webservice.stereotype.POST;
import io.vertigo.vega.webservice.stereotype.PathParam;
import io.vertigo.vega.webservice.stereotype.PathPrefix;
import io.vertigo.vega.webservice.stereotype.QueryParam;

@PathPrefix("/heaters")
public class HeaterWebServices implements WebServices {

	@Inject
	private HeaterControlServices heaterControlServices;

	@GET("")
	public DtList<Heater> listHeaters() {
		return heaterControlServices.listAllHeaters();

	}

	@POST("/{heaId}/_changeMode")
	public void changeMode(@PathParam("heaId") final Long heaId, @QueryParam("mode") final String heaterMode) {
		heaterControlServices.changeHeaterMode(heaId, HeaterMode.valueOf(heaterMode));

	}

}
