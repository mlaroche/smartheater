package com.mlaroche.smartheater.domain;

import java.io.Serializable;

import io.vertigo.dynamo.domain.model.MasterDataEnum;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

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
	public URI<com.mlaroche.smartheater.domain.HeaterMode> getEntityUri() {
		return DtObjectUtil.createURI(com.mlaroche.smartheater.domain.HeaterMode.class, entityId);
	}

}
