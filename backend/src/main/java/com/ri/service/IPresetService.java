package com.ri.service;
import java.util.List;

import com.ri.model.preset.RIPreset;

public interface IPresetService
{
	public List<RIPreset> getPresets();
	public RIPreset getInit();
	

}
