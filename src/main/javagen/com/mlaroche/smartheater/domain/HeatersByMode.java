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

import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class HeatersByMode implements DtObject {
	private static final long serialVersionUID = 1L;

	private String mode;
	private Integer count;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return String mode
	 */
	@Field(domain = "DO_LABEL", label = "Id")
	public String getMode() {
		return mode;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Id'.
	 * @param mode String
	 */
	public void setMode(final String mode) {
		this.mode = mode;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nombre'.
	 * @return Integer count
	 */
	@Field(domain = "DO_NUMBER", label = "Nombre")
	public Integer getCount() {
		return count;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nombre'.
	 * @param count Integer
	 */
	public void setCount(final Integer count) {
		this.count = count;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}