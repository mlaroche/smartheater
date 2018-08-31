package com.mlaroche.smartheater.services;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.ProtocolEnum;
import com.mlaroche.smartheater.model.HeaterMode;

import io.vertigo.core.component.Plugin;

public interface RemoteHeaterControlerPlugin extends Plugin {
	
	void changeHeaterMode(final Heater heater, final HeaterMode heaterMode);
	
	ProtocolEnum getProtocol();

}
