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

import com.mlaroche.smartheater.domain.DtDefinitions;

import io.vertigo.app.AutoCloseableApp;
import io.vertigo.app.config.AppConfig;
import io.vertigo.app.config.DefinitionProviderConfig;
import io.vertigo.app.config.ModuleConfig;
import io.vertigo.commons.impl.CommonsFeatures;
import io.vertigo.commons.plugins.cache.memory.MemoryCachePlugin;
import io.vertigo.core.param.Param;
import io.vertigo.core.plugins.param.properties.PropertiesParamPlugin;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.core.plugins.resource.url.URLResourceResolverPlugin;
import io.vertigo.database.DatabaseFeatures;
import io.vertigo.database.plugins.sql.connection.c3p0.C3p0ConnectionProviderPlugin;
import io.vertigo.dynamo.impl.DynamoFeatures;
import io.vertigo.dynamo.plugins.environment.DynamoDefinitionProvider;
import io.vertigo.dynamo.plugins.store.datastore.sql.SqlDataStorePlugin;
import io.vertigo.vega.VegaFeatures;
import spark.Spark;

public class Main {

	private static final AppConfig buildAppConfig(final String confUrl) {
		return AppConfig.builder()
				.beginBoot()
				.withLocales("fr_FR")
				.addPlugin(ClassPathResourceResolverPlugin.class)
				.addPlugin(URLResourceResolverPlugin.class)
				.addPlugin(PropertiesParamPlugin.class,
						Param.of("url", confUrl))
				.endBoot()
				.addModule(new CommonsFeatures()
						.withScript()
						.withCache(MemoryCachePlugin.class)
						.build())
				.addModule(new DatabaseFeatures()
						.withSqlDataBase()
						.addSqlConnectionProviderPlugin(C3p0ConnectionProviderPlugin.class,
								Param.of("dataBaseClass", "io.vertigo.database.impl.sql.vendor.h2.H2DataBase"),
								Param.of("jdbcDriver", "org.h2.Driver"),
								Param.of("jdbcUrl", "${h2_db_url}"))
						.build())
				.addModule(new DynamoFeatures()
						.withStore()
						.addDataStorePlugin(SqlDataStorePlugin.class,
								Param.of("sequencePrefix", "SEQ_"))
						.build())
				.addModule(new VegaFeatures()
						.withEmbeddedServer(8080)
						.withApiPrefix("/api")
						.build())
				.addModule(new SmartHeaterFeature().build())
				.addModule(ModuleConfig.builder("definitions")
						.addDefinitionProvider(DefinitionProviderConfig.builder(DynamoDefinitionProvider.class)
								.addDefinitionResource("classes", DtDefinitions.class.getName())
								.addDefinitionResource("kpr", "com/mlaroche/smartheater/domain/execution.kpr")
								.addParam(Param.of("encoding", "utf8"))
								.build())
						.build())
				//.addInitializer(DataBaseInitializer.class)
				.build();

	}

	public static void main(final String[] args) throws InterruptedException {
		Spark.staticFileLocation("/web");
		try (final AutoCloseableApp app = new AutoCloseableApp(buildAppConfig(args[0]))) {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(10 * 1000);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
