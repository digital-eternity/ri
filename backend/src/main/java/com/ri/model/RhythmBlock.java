package com.ri.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
 * - note
 * - 
 */
@Entity
@Table(name = "RHYTHM_BLOCK")
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String label = "";
	private Boolean isStrong = false;
	private Integer subDivision = DEFAULT_SUBDIVISION;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "RHYTHM_BLOCK_ID", insertable = true, updatable = true)
    private List<RhythmElement> rhythmElements = new LinkedList<>();
	
	
	public RhythmBlock()
	{
		rhythmElements = IntStream.range( 0, subDivision ).
				boxed().
				map( i -> new RhythmElement() ).
				toList();
	}
	
	public RhythmBlock( ERhythmElementType type, ERhythmElementDuration duration )
	{
		rhythmElements.add( new RhythmElement() );
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
	
	public List<RhythmElement> getRhythmElements()
	{
		return rhythmElements;
	}
	
	public void setRhythmElements( List<RhythmElement> rhythmElements )
	{
		this.rhythmElements = rhythmElements;
	}

	public Integer getSubDivision()
	{
		return subDivision;
	}

	public void setSubDivision( Integer count )
	{
		this.subDivision = count;
		
		//TODO: optimize
		ERhythmElementDuration dur = rhythmElements.get( 0 ).getDuration();
		
		for ( int i = 0; i < count; i++ )
		{
			rhythmElements.add( new RhythmElement( RhythmElement.DEFAULT_ELEMENT_TYPE, dur ) );
		}
	}
	
	
	public void changeElementType()
	{
		changeElementType(0);
	}
	
	
	public void changeElementType( Integer n )
	{
		rhythmElements.get( n ).changeType();
	}
	
	public void setRhythmElement( RhythmElement el )
	{
		setRhythmElement( 0, el );
	}
	
	
	public void setRhythmElement( Integer n, RhythmElement el )
	{
		rhythmElements.set( n, el );
	}
	
	@Override
	//TODO: check and update
	public boolean equals( Object o ) 
	{
		if( this == o )
			return true;
	    
		if( !( o instanceof RhythmBlock ) )
			return false;
	    
		RhythmBlock block = ( RhythmBlock ) o;
	    
		return Objects.equals( id, block.getId() );
	  }
	
	@Override
	public int hashCode() 
	{
		return Objects.hash( this.id, this.rhythmElements );
	}


}
