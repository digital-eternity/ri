package com.ri.service;
import java.util.List;

public interface IPresetService
{
	public List<String> getPresets();
	
	public String getInit();
	
	public List<String> getValsForPreset( String name );
}
