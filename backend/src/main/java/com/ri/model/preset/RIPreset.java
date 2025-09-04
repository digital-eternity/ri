package com.ri.model.preset;

import java.util.List;

public enum RIPreset
{
	CLASSIC("Classic"),
	BODY_PERCUSSION("Body Percussion"),
	KONNAKOL("Konnakol");
	
	private String name;
		
	private RIPreset( String name )
	{
		// TODO Auto-generated constructor stub
		this.name = name;
		
		List<> l = Class.forName( name + "Preset" ).values();
	}
	
	
	public String getName()
	{
		return name;
	}
	public void setName( String name )
	{
		this.name = name;
	}
	
	/**
	 * A common method for all enums since they can't have another base class
	 * @param <T> Enum type
	 * @param c enum type. All enums must be all caps.
	 * @param string case insensitive
	 * @return corresponding enum, or null
	 */
	public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) 
	{
	    if( c != null && string != null ) {
	        try {
	            return Enum.valueOf(c, string.trim().toUpperCase());
	        } catch(IllegalArgumentException ex) {
	        }
	    }
	    return null;
	}
	
	


}
