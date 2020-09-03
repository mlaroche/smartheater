package com.mlaroche.smartheater.mda;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import io.vertigo.core.lang.WrappedException;
import io.vertigo.studio.tools.VertigoStudioMda;

public class Studio {

	//	private static NodeConfig buildNodeConfig() {
//		// @formatter:off
//		return  NodeConfig.builder()
//				.beginBoot()
//				.withLocales("fr_FR")
//				.addPlugin(ClassPathResourceResolverPlugin.class)
//				.endBoot()
//				.addModule(new CommonsFeatures()
//						.withCache()
//						.withMemoryCache()
//						.withScript()
//						.withJaninoScript()
//						.build())
//				.addModule(new DynamoFeatures().build())
//				//----Definitions
//				.addModule(ModuleConfig.builder("ressources")
//						.addDefinitionProvider(DefinitionProviderConfig.builder(DynamoDefinitionProvider.class)
//								.addParam(Param.of("encoding", "UTF-8"))
//								.addDefinitionResource("kpr", "com/mlaroche/smartheater/domain/generation.kpr")
//								.build())
//						.build())
//				// ---StudioFeature
//				.addModule( new StudioFeatures()
//					.withMasterData()
//					.withMda(Param.of("projectPackageName", "com.mlaroche.smartheater"))
//					.withJavaDomainGenerator(
//							Param.of("generateDtResources", "false"))
//					.withTaskGenerator()
//					.withFileGenerator()
//					.withSqlDomainGenerator(
//							Param.of("targetSubDir", "javagen/sqlgen"),
//							Param.of("baseCible", "H2"),
//							Param.of("generateDrop", "false"),
//							Param.of("generateMasterData", "true"))
//					.withJsonMasterDataValuesProvider(
//							Param.of("fileName" ,"com/mlaroche/smartheater/domain/data/JsonMasterDataValues.json"))
//					.build())
//				.build();
//		// @formatter:on
	//
	//	}

	public static void main(final String[] args) {
		try {
			VertigoStudioMda.main(new String[] { "generate", Paths.get("studio-config.yaml").toUri().toURL().toExternalForm() });
		} catch (final MalformedURLException e) {
			throw WrappedException.wrap(e);
		}
	}
}
