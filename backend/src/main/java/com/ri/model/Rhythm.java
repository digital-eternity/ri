package com.ri.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Rhythm 
{
	public final static ERhythmElementDuration DEFAULT_DURATION = ERhythmElementDuration.QUARTER;
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name = "Rhythm ";
	private String description = "";

	private Integer size;

	@Enumerated(EnumType.STRING)
	private ERhythmElementDuration baseNote = DEFAULT_DURATION;
	
	@OneToMany(cascade = CascadeType.ALL)
	//@JoinColumn(name = "rhythm_id")
	private List<RhythmBlock> aRhythm;
	
	
	public Rhythm()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor creates rhythms with predefined size
	 * 
	 * @param size
	 */
	public Rhythm( Integer size )
	{
		this.size = size;
		
		aRhythm = IntStream.range( 0, size ).
				boxed().
				map( i -> new RhythmBlock( ERhythmElementType.PAUSE, baseNote ) ).
				toList();	
		
		String srtt = "";
	}
	
	
	/*
	private void initWithSubdivisionLabeles( Integer size, Integer subDiv )
	{
		aRhythm = new ArrayList<RhythmBlock>( ( size * subDiv ) );
		 
		RhythmBlock block;
		
		for( int i = 0; i < size; i++ )
		{
			block = new RhythmBlock();
			block.setLabel( String.valueOf( i + 1 ) );
			
			aRhythm.add( block ); 
			
			for ( int j = 0; j < subDiv-1; j++ )
			{
				block = new RhythmBlock();
				block.setLabel( SUBDIVISION_NAMES[subDiv][j] );
				aRhythm.add( block ); 				
			}
		}
	}
	 */
	
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
		this.name= name ;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription( String descr )
	{
		this.description = descr;
	}

	public Integer getSize()
	{
		return size;
	}
	
	protected void setSize( Integer s )
	{
		this.size = s;
	}
	
	public void setNote( Integer n, 
			RhythmElement rhythmElement )
	{
		RhythmBlock rhythmBlock = aRhythm.get( n );
		
		rhythmBlock.setRhythmElement( rhythmElement );
	}
	

	public void setNote( Integer n )
	{
		RhythmBlock rhythmBlock = aRhythm.get( n );
		
		rhythmBlock.getRhythmElement().changeType();
	}
	
	@Override
	public boolean equals( Object o ) 
	{
		if( this == o )
			return true;
	    
		if( !( o instanceof Rhythm ) )
			return false;
	    
		Rhythm r = ( Rhythm ) o;
	    
		return Objects.equals( id, r.getId() ) && Objects.equals( name, r.getName() )
				&& Objects.equals( size, r.getSize() );
	  }
	
	@Override
	public int hashCode() 
	{
		return Objects.hash( this.id, this.name, this.size );
	}
	
	
	@Override
	public String toString() 
	{
		return String.format(
				"Rhythm[id=%d, name='%s', rhythm=%s]",
				id, name, aRhythm.toString() );
	}
	
}
