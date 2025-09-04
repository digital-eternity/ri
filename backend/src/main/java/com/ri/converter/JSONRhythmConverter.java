package com.ri.converter;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ri.model.rhythm.Rhythm;


/**
 * Represents serialize helper for JSON, represents objects in String format
 * using native {@code Object#toString()} method.
 * 
 */
public final class JSONRhythmConverter 
{
	private final static String JSON_FILE = "rhythm.json";
	
	/**
	 * 
	 * @param value
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static void writeRhythm( Object value )
			throws IOException, JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.writeValue( new File( JSON_FILE ) , value );
		
	}
	
	/**
	 * Reading Rhythm from JSON file
	    
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static Rhythm readRythmFromFile() 
			throws IOException, JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();

	    return mapper
	    		.readValue( new File( JSON_FILE ), Rhythm.class );   
	}	
}
