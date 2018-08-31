package com.mlaroche.smartheater.boot;

import com.mlaroche.smartheater.domain.DtDefinitions;

import io.vertigo.app.AutoCloseableApp;
import io.vertigo.app.config.AppConfig;
import io.vertigo.app.config.DefinitionProviderConfig;
import io.vertigo.app.config.ModuleConfig;
import io.vertigo.app.config.discovery.ModuleDiscoveryFeatures;
import io.vertigo.commons.impl.CommonsFeatures;
import io.vertigo.commons.plugins.cache.memory.MemoryCachePlugin;
import io.vertigo.core.param.Param;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.core.plugins.resource.url.URLResourceResolverPlugin;
import io.vertigo.database.DatabaseFeatures;
import io.vertigo.database.plugins.sql.connection.c3p0.C3p0ConnectionProviderPlugin;
import io.vertigo.dynamo.impl.DynamoFeatures;
import io.vertigo.dynamo.plugins.environment.DynamoDefinitionProvider;
import io.vertigo.dynamo.plugins.store.datastore.sql.SqlDataStorePlugin;
import io.vertigo.vega.VegaFeatures;

public class Main {

	private static final AppConfig buildAppConfig() {
		return AppConfig.builder()
				.beginBoot()
				.withLocales("fr_FR")
				.addPlugin(ClassPathResourceResolverPlugin.class)
				.addPlugin(URLResourceResolverPlugin.class)
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
								Param.of("jdbcUrl", "jdbc:h2:mem:database"))
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
				.addModule(new ModuleDiscoveryFeatures("smartheater") {

					@Override
					protected String getPackageRoot() {
						return "com.mlaroche.smartheater";
					}
				}.build())
				.addModule(ModuleConfig.builder("definitions")
						.addDefinitionProvider(DefinitionProviderConfig.builder(DynamoDefinitionProvider.class)
								.addDefinitionResource("classes", DtDefinitions.class.getName())
								.addDefinitionResource("kpr", "com/mlaroche/smartheater/domain/execution.kpr")
								.addParam(Param.of("encoding", "utf8"))
								.build())
						.build())
				.addInitializer(DataBaseInitializer.class)
				.build();

	}

	public static void main(final String[] args) throws InterruptedException {
		try (final AutoCloseableApp app = new AutoCloseableApp(buildAppConfig())) {
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
