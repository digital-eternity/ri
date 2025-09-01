package com.ri.model;

import java.util.List;
import java.util.stream.Stream;


public enum ERhythmElementDuration
{ 
	WHOLE( 1, 4.0 ), 
	HALF( 2, 2.0 ), 
	QUARTER( 3, 1.0 ), 
	EIGHT( 4, 0.5 ),
	SIXTEENTH ( 5, 0.25 ),
	THIRTYSECOND( 6, 0.125 );

	private Integer hierarchy; 
	private Double duration;
	
	private ERhythmElementDuration( Integer h, Double s )
	{
		hierarchy = h;
		duration = s;
	}
	
	public Double getDuration()
	{
		return duration;
	}
	
	public ERhythmElementDuration getNext()
	{
		return getValues().get( hierarchy + 1 );
	}
	
	public List<ERhythmElementDuration> getValues()
	{
		return Stream.of( values() )
					.sorted( 
						(o1, o2) ->  o1.hierarchy.compareTo( o2.hierarchy ) )
					.toList();
	}
}
