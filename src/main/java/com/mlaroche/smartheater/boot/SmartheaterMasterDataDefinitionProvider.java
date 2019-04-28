/**
 *
 */
package com.mlaroche.smartheater.boot;

import com.mlaroche.smartheater.domain.HeaterMode;
import com.mlaroche.smartheater.domain.Protocol;

import io.vertigo.dynamo.impl.store.datastore.AbstractMasterDataDefinitionProvider;

/**
 * Init masterdata definition.
 * @author mlaroche,jmforhan
 */
public class SmartheaterMasterDataDefinitionProvider extends AbstractMasterDataDefinitionProvider {

	@Override
	public void declareMasterDataLists() {
		registerDtMasterDatas(HeaterMode.class);
		registerDtMasterDatas(Protocol.class);
	}

}
