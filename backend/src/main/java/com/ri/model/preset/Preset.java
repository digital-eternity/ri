package com.ri.model.preset;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Preset
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	protected String name;
	protected List<String> elements;
		
	protected Preset( )
	{
	}
	
	public Long getId() 
	{
		return id;
	}
	
	public void setId( Long id ) 
	{
		this.id = id;
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
