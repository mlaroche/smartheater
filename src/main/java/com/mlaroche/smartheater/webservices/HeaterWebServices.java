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
