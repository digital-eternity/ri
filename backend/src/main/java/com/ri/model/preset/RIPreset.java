package com.ri.model.preset;

import java.util.List;

public class RIPreset
{
	protected String name;
	protected List<String> elements;
		
	protected RIPreset( )
	{
	}

	public String getName()
	{
		return name;
	}
	public void setName( String name )
	{
		this.name = name;
	}

	public List<String> getElements()
	{
		return elements;
	}

	protected void setElements( List<String> elements )
	{
		this.elements = elements;
	}	


}
