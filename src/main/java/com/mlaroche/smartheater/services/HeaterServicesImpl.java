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

import com.mlaroche.smartheater.dao.HeaterDAO;
import com.mlaroche.smartheater.dao.heater.HeaterPAO;
import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterModeEnum;
import com.mlaroche.smartheater.domain.HeatersByMode;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.DtListState;

@Transactional
public class HeaterServicesImpl implements HeaterServices {

	@Inject
	private HeaterDAO heaterDAO;
	@Inject
	private HeaterPAO heaterPAO;

	@Override
	public Heater saveHeater(final Heater heater) {
		Assertion.check().isNotNull(heater);
		//---
		return heaterDAO.save(heater);
	}

	@Override
	public Heater getHeater(final Long heaId) {
		Assertion.check().isNotNull(heaId);
		//---
		return heaterDAO.get(heaId);

	}

	@Override
	public DtList<Heater> listHeaters() {
		return heaterDAO.findAll(Criterions.alwaysTrue(), DtListState.of(null));
	}

	@Override
	public DtList<HeatersByMode> getHeatersByMode() {
		return heaterPAO.getHeatersByMode();
	}

	@Override
	public Heater initHeater() {
		final Heater heater = new Heater();
		heater.mode().setEnumValue(HeaterModeEnum.arret);
		return heater;
	}

}
