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

import javax.inject.Inject;

import com.mlaroche.smartheater.dao.ProtocolDAO;
import com.mlaroche.smartheater.domain.Protocol;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.DtListState;

@Transactional
public class ProtocolServicesImpl implements ProtocolServices {

	@Inject
	private ProtocolDAO protocolDAO;

	@Override
	public Protocol saveProtocol(final Protocol protocol) {
		Assertion.check().isNotNull(protocol);
		//---
		return protocolDAO.save(protocol);
	}

	@Override
	public Protocol getProtocol(final String proCd) {
		Assertion.check().isNotNull(proCd);
		//---
		return protocolDAO.get(proCd);

	}

	@Override
	public DtList<Protocol> listProtocols() {
		return protocolDAO.findAll(Criterions.alwaysTrue(), DtListState.of(null));
	}

}
