package com.mlaroche.smartheater.domain;

import java.io.Serializable;

import io.vertigo.datamodel.structure.model.MasterDataEnum;
import io.vertigo.datamodel.structure.model.UID;

public enum HeaterModeEnum implements MasterDataEnum<com.mlaroche.smartheater.domain.HeaterMode> {

	horsgel("horsgel"), //
	arret("arret"), //
	confort("confort"), //
	eco("eco")
	;

	private final Serializable entityId;

	private HeaterModeEnum(final Serializable id) {
		entityId = id;
	}

	@Override
	public UID<com.mlaroche.smartheater.domain.HeaterMode> getEntityUID() {
		return UID.of(com.mlaroche.smartheater.domain.HeaterMode.class, entityId);
	}

}
