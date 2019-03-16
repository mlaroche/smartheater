package com.mlaroche.smartheater.services;

import java.util.List;

import com.mlaroche.smartheater.domain.HeaterInfo;

import io.vertigo.core.component.Component;

public interface InfosServices extends Component {

	List<HeaterInfo> getInfos();

}
