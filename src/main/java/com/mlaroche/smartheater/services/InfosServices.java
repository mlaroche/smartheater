package com.mlaroche.smartheater.services;

import java.util.List;

import com.mlaroche.smartheater.domain.Heater;
import com.mlaroche.smartheater.domain.HeaterInfo;

import io.vertigo.core.component.Component;
import io.vertigo.database.timeseries.TimedDatas;

public interface InfosServices extends Component {

	List<HeaterInfo> getInfos();

	TimedDatas getWeekElectricalData();

	TimedDatas getWeekMeanIndoorTemperature();

	TimedDatas getWeekMeanOutdoorTemperature();

	TimedDatas getLastDayHeaterInfos(Heater heater);

}
