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
package com.mlaroche.smartheater.boot;

import io.vertigo.app.config.discovery.ModuleDiscoveryFeatures;

public final class SmartHeaterFeature extends ModuleDiscoveryFeatures {

	public SmartHeaterFeature() {
		super("smartheater");
	}

	@Override
	protected String getPackageRoot() {
		return "com.mlaroche.smartheater";
	}
}
