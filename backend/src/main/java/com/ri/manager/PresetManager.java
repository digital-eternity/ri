package com.ri.manager;

import java.util.List;

import com.ri.model.preset.BodyPercussionPreset;
import com.ri.model.preset.ClassicPreset;
import com.ri.model.preset.HandpanStylePreset;
import com.ri.model.preset.RIPreset;


public class PresetManager implements IPresetManager
{
	private final static List<String> presets = List.of(
			"Classic", 
			"Body Percussion", 
			"Handpan Style");
	
	public List<String> getAvailablePresets()
	{
		return presets;	
	}

	public String getInit()
	{
		return presets.get( 0 );
	}

	public List<String> getPresetValues( String name )
	{
		RIPreset preset;
		
		switch ( name )
		{
			case "Classic":
				preset = new ClassicPreset();
				break;
			case "BodyPercussion":
				preset = new BodyPercussionPreset();
			case "HandpanStylePreset":
				preset = new HandpanStylePreset();	
			default:
				preset = new ClassicPreset();
		}
		
		return preset.getElements();
	}

}
