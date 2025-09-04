package com.ri.api;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ri.model.preset.RIPreset;
import com.ri.service.IPresetService;
import com.ri.service.PresetService;

@RestController
@RequestMapping("/api/preset")
@Validated
public class PresetController
{
	
    private final IPresetService service; 

    public PresetController( PresetService service ) 
    { 
    	this.service = service;
    }
		
   	@GetMapping("/getAll")
	public List<String> getPresets() 
	{
		return service.getPresets();
	}
	
	@GetMapping("/getInit")
	public String getInit() 
	{
		return service.getInit();
	}
	
	@GetMapping("/getPresetValues/{name}")
	public List<String> getPresetValues( String name ) 
	{
		return service.getValsForPreset( name );
	}

}
