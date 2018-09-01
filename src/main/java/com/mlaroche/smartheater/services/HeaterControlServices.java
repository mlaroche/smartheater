package com.mlaroche.smartheater.services;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.model.HeaterMode;

import io.vertigo.core.component.Activeable;
import io.vertigo.core.component.Component;
import io.vertigo.dynamo.domain.model.DtList;

public interface HeaterControlServices extends Component, Activeable {

	void changeHeaterMode(Long heaId, HeaterMode heaterMode);

	DtList<Heater> listAllHeaters();

}
