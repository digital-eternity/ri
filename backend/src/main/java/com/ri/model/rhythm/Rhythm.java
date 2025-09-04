package com.ri.model.rhythm;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "RHYTHM")
public class Rhythm 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name = "Rhythm ";
	private String description = "";

	private Integer size;

	@Enumerated(EnumType.STRING)
	private ERhythmElementDuration baseNote = RhythmElement.DEFAULT_ELEMENT_DURATION;
	
	//@OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "RHYTHM_ID", insertable = true, updatable = true)
    private List<RhythmBlock> rhythm;
	
	
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
		
		rhythm = IntStream.range( 0, size ).
				boxed().
				map( i -> new RhythmBlock() ).
				toList();	
		
		String srtt = "";
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
	
	public ERhythmElementDuration getBaseNote()
	{
		return baseNote;
	}
	
	public void setBaseNote( ERhythmElementDuration dur )
	{
		this.baseNote = dur;
		
		/** 
		 * TODO: если baseNote < dur
		 */
		
	}

	public Integer getSize()
	{
		return size;
	}
	
	protected void setSize( Integer s )
	{
		this.size = s;
	}
	
	//TODO: to add functionality to update subdivisions
	public void setNote( Integer n, 
			RhythmElement rhythmElement )
	{
		RhythmBlock rhythmBlock = rhythm.get( n );
		
		rhythmBlock.setRhythmElement( rhythmElement );
	}
	

	public void setNote( Integer n )
	{
		RhythmBlock rhythmBlock = rhythm.get( n );
		
		rhythmBlock.changeElementType();
	}
	
	public List<RhythmBlock> getRhythm()
	{
		return rhythm;
	}
	
	public void setRhythm( List<RhythmBlock> rhythm )
	{
		this.rhythm = rhythm;
	}
	
	@Override
	//TODO: update
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
				id, name, rhythm.toString() );
	}
	
}
