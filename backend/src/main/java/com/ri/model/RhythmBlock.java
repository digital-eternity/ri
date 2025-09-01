package com.ri.model;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

/*
 * - note
 * - 
 */
@Entity
public class RhythmBlock
{
	public final static Integer DEFAULT_SUBDIVISION = 1;
	
	private final static String[][] SUBDIVISION_NAMES = {
			{},
			{},
			{"&"}, 
			{"&", "a"},
			{"e", "&", "a"}};
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String label;
	private Boolean isStrong = false;
	private Integer subDivision = DEFAULT_SUBDIVISION;
	
	@OneToOne(cascade = CascadeType.ALL)
	private RhythmElement rhythmElement;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<RhythmElement> blockSubdivisions;
	
	
	protected RhythmBlock()
	{
		rhythmElement = new RhythmElement();
	}
	
	public RhythmBlock( ERhythmElementType type, ERhythmElementDuration duration )
	{
		rhythmElement = new RhythmElement( type, duration );
	}

	public Long getId()
	{
		return id;
	}

	public void setId( Long id )
	{
		this.id = id;
	}
	
	public String getLabel()
	{
		return this.label;
	}

	public void setLabel( String l )
	{
		this.label = l;
	}
	
	public Boolean getIsStrong()
	{
		return isStrong;
	}

	public void setIsStrong( Boolean isStrong )
	{
		this.isStrong = isStrong;
	}
	
	public RhythmElement getRhythmElement()
	{
		return rhythmElement;
	}
	
	public void setRhythmElement( RhythmElement rhythmElement )
	{
		this.rhythmElement = rhythmElement;
	}

	/*
	public ERhythmElementType getType()
	{
		return type;
	}

	public void setElementType( ERhythmElementType type )
	{
		this.type = type;
	}
	
	public ERhythmElementDuration getDuration()
	{
		return this.duration;
	}
	
	public void setDuration( ERhythmElementDuration dur )
	{
		if( type == DEFAULT_ELEMENT_TYPE || ( dur.getDuration() < duration.getDuration() ) )
		{
			//TODO: create subdiv list and set note
			this.duration = dur;
		}
	}
	*/

	public Integer getSubDivision()
	{
		return subDivision;
	}

	public void setSubDivision( Integer count )
	{
		this.subDivision = count;
		
		//TODO: optimize
		ERhythmElementDuration dur = rhythmElement.getDuration().getNext();
		
		setBlockSubdivisions( IntStream.range( 0, count ).
				boxed().
				map( i -> new RhythmElement( ERhythmElementType.PAUSE, dur ) ).
				toList() );
	}

	public List<RhythmElement> getBlockSubdivisions()
	{
		return blockSubdivisions;
	}

	public void setBlockSubdivisions( List<RhythmElement> blockSubdivisions )
	{
		this.blockSubdivisions = blockSubdivisions;
	}
}
