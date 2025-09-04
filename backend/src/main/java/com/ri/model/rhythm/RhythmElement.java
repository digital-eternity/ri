package com.ri.model.rhythm;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "RHYTHM_ELEMENT")
public class RhythmElement
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public final static ERhythmElementType DEFAULT_ELEMENT_TYPE = ERhythmElementType.PAUSE;
	public final static ERhythmElementDuration DEFAULT_ELEMENT_DURATION = ERhythmElementDuration.QUARTER;
	
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
	
	@Override
	public boolean equals( Object o ) 
	{
		if( this == o )
			return true;
	    
		if( !( o instanceof RhythmElement ) )
			return false;
	    
		RhythmElement el = ( RhythmElement ) o;
	    
		return Objects.equals( type, el.getType() )
				&& Objects.equals( duration, el.getDuration() );
	  }
	
	@Override
	public int hashCode() 
	{
		return Objects.hash( this.id, this.type, this.duration );
	}
}
