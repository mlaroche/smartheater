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
