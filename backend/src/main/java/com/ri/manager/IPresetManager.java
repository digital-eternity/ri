package com.ri.manager;

import java.util.List;

public interface IPresetManager
{
	
	public String getInit();
	
	public List<String> getPresetValues( String name );

	public List<String> getAvailablePresets();
	
}
