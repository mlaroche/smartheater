package com.mlaroche.smartheater.domain;

import java.io.Serializable;

import io.vertigo.dynamo.domain.model.MasterDataEnum;
import io.vertigo.dynamo.domain.model.UID;

public enum ProtocolEnum implements MasterDataEnum<com.mlaroche.smartheater.domain.Protocol> {

	http("HTTP")
	;

	private final Serializable entityId;

	private ProtocolEnum(final Serializable id) {
		entityId = id;
	}

	@Override
	public UID<com.mlaroche.smartheater.domain.Protocol> getEntityUID() {
		return UID.of(com.mlaroche.smartheater.domain.Protocol.class, entityId);
	}

}
