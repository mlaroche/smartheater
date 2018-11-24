package com.mlaroche.smartheater.services;

import java.util.List;

import com.mlaroche.smartheater.model.HeaterInfo;

import io.vertigo.core.component.Component;

public interface InfosServices extends Component {

	List<HeaterInfo> getInfos();

}
