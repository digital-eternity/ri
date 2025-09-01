package com.ri.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RhythmElement
{
	public final static ERhythmElementType DEFAULT_ELEMENT_TYPE = ERhythmElementType.PAUSE;
	public final static ERhythmElementDuration DEFAULT_ELEMENT_DURATION = ERhythmElementDuration.QUARTER;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private ERhythmElementType type; 
	@Enumerated(EnumType.STRING)
	private ERhythmElementDuration duration;
	
	public RhythmElement()
	{
		type = DEFAULT_ELEMENT_TYPE;
		duration = DEFAULT_ELEMENT_DURATION;
	}
	
	public RhythmElement( ERhythmElementType type, 
			ERhythmElementDuration duration )
	{
		this.setType( type );
		this.setDuration( duration );
	}
	
	public void changeType()
	{
		if( type == ERhythmElementType.PAUSE )
			type = ERhythmElementType.NOTE;
		else
			type = ERhythmElementType.PAUSE;
	}

	public ERhythmElementType getType()
	{
		return type;
	}

	public void setType( ERhythmElementType type )
	{
		this.type = type;
	}

	public ERhythmElementDuration getDuration()
	{
		return duration;
	}

	public void setDuration( ERhythmElementDuration duration )
	{
		this.duration = duration;
	}
}
