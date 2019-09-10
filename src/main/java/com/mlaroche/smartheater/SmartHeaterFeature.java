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
package com.mlaroche.smartheater;

import com.mlaroche.smartheater.boot.SmartheaterMasterDataDefinitionProvider;
import com.mlaroche.smartheater.domain.DtDefinitions;

import io.vertigo.app.config.DefinitionProviderConfig;
import io.vertigo.app.config.discovery.ModuleDiscoveryFeatures;
import io.vertigo.core.param.Param;
import io.vertigo.dynamo.plugins.environment.DynamoDefinitionProvider;

public final class SmartHeaterFeature extends ModuleDiscoveryFeatures<SmartHeaterFeature> {

	public SmartHeaterFeature() {
		super("smartheater");
	}

	@Override
	protected String getPackageRoot() {
		return "com.mlaroche.smartheater";
	}

	@Override
	protected void buildFeatures() {
		super.buildFeatures();
		//----
		getModuleConfigBuilder().addDefinitionProvider(DefinitionProviderConfig.builder(DynamoDefinitionProvider.class)
				.addDefinitionResource("classes", DtDefinitions.class.getName())
				.addDefinitionResource("kpr", "com/mlaroche/smartheater/domain/execution.kpr")
				.addParam(Param.of("encoding", "utf8"))
				.build());
		getModuleConfigBuilder().addDefinitionProvider(SmartheaterMasterDataDefinitionProvider.class);
	}
}
