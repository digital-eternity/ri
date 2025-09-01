package com.ri.controller;

public class RhythmNotFoundException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3489817047404356920L;

	public RhythmNotFoundException( Long id )
	{
	    super("Could not find employee " + id);
		
	}

}
