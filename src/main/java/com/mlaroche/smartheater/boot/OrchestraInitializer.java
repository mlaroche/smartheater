package com.mlaroche.smartheater.boot;

import javax.inject.Inject;

import com.mlaroche.smartheater.jobs.EnedisElectricalConsumptionActivityEngine;

import io.vertigo.core.component.ComponentInitializer;
import io.vertigo.orchestra.definitions.OrchestraDefinitionManager;
import io.vertigo.orchestra.definitions.ProcessDefinition;
import io.vertigo.orchestra.definitions.ProcessType;

/**
 * Initialisation des processus gérés par Orchestra
 *
 * @author mlaroche.
 * @version $Id$
 */
public class OrchestraInitializer implements ComponentInitializer {

	@Inject
	private OrchestraDefinitionManager orchestraDefinitionManager;

	/** {@inheritDoc} */
	@Override
	public void init() {

		final ProcessDefinition enedisRetrieval = ProcessDefinition.builder("ProEnedisElectricalConsumption", "Recupération de information de consommation electrique")
				.withProcessType(ProcessType.UNSUPERVISED)
				.withCronExpression("0 0 6 ? * * *")
				.addActivity("retrieveData", "retriveData", EnedisElectricalConsumptionActivityEngine.class)
				.build();
		orchestraDefinitionManager.createOrUpdateDefinition(enedisRetrieval);

	}

}
