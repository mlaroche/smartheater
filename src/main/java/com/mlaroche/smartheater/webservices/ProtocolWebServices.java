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
package com.mlaroche.smartheater.webservices;

import javax.inject.Inject;

import com.mlaroche.smartheater.domain.Protocol;
import com.mlaroche.smartheater.services.ProtocolServices;

import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.vega.webservice.WebServices;
import io.vertigo.vega.webservice.stereotype.GET;
import io.vertigo.vega.webservice.stereotype.PathParam;
import io.vertigo.vega.webservice.stereotype.PathPrefix;

@PathPrefix("/protocols")
public class ProtocolWebServices implements WebServices {

	@Inject
	private ProtocolServices protocolServices;

	@GET("")
	public DtList<Protocol> listProtocols() {
		return protocolServices.listProtocols();

	}

	@GET("/{proCd}")
	public Protocol getProtocol(@PathParam("proCd") final String proCd) {
		return protocolServices.getProtocol(proCd);

	}

}
