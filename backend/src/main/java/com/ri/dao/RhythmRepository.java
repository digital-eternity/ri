package com.ri.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ri.model.rhythm.Rhythm;

@Repository
public interface RhythmRepository extends CrudRepository<Rhythm, Long> 
{
	/**
	 * Finds all rhythms to appropriate size
	 * 
	 * @param size
	 *            size of rhythms

	 * @return the list of {@Rhythms} entities
	 */
	Rhythm findByName( String name );

	Rhythm findById( long id );
	
	//List<Rhythm> getAll();
}
