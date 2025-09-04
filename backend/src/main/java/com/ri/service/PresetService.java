package com.ri.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ri.manager.IPresetManager;
import com.ri.manager.PresetManager;


@Service
public class PresetService implements IPresetService
{
	IPresetManager manager;
	
	public PresetService( PresetManager mngr )
	{
		manager = mngr;
	}

	@Override
	public List<String> getPresets()
	{
		return manager.getAvailablePresets();
	}

	@Override
	public String getInit()
	{
		return manager.getInit();
	}

	@Override
	public List<String> getValsForPreset( String name )
	{
		return manager.getPresetValues( name );
	}

}
