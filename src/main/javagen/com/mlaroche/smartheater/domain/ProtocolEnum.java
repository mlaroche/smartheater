package com.mlaroche.smartheater.domain;

import java.io.Serializable;

import io.vertigo.dynamo.domain.model.MasterDataEnum;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

public enum ProtocolEnum implements MasterDataEnum<com.mlaroche.smartheater.domain.Protocol> {

	http("HTTP")
	;

	private final Serializable entityId;

	private ProtocolEnum(final Serializable id) {
		entityId = id;
	}

	@Override
	public URI<com.mlaroche.smartheater.domain.Protocol> getEntityUri() {
		return DtObjectUtil.createURI(com.mlaroche.smartheater.domain.Protocol.class, entityId);
	}

}
