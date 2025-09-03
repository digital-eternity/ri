package com.ri.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import com.ri.dao.RhythmRepository;
import com.ri.model.Rhythm;

/**
 * RI Rest API controller
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/ri")
@Validated
public class RIController 
{
    private final RhythmRepository repository; 

    public RIController( RhythmRepository rep ) 
    { 
    	this.repository = rep;
    }
	
    @PostMapping("/addRhythm")
    public Rhythm addRhythm( @RequestBody Rhythm newRhythm ) 
	{
		return repository.save( newRhythm );
	}
    
    //TODO: add validation and fix updating
    @PutMapping("/replaceRhythm/{id}")
    public Rhythm updateRhythm( @RequestBody Rhythm newRhythm, 
    							@PathVariable Long id ) 
	{
		return repository.findById(id)
			      .map(rhythm -> 
			      {
			        rhythm.setName( newRhythm.getName() );
			        
			        return repository.save(rhythm);
			      })
			      .orElseGet(() -> 
			      {
			        return repository.save(newRhythm);
			      });
	}
	
    @GetMapping("/getRhythm/{id}")
    @CrossOrigin(origins="http://localhost:8081")
	public Rhythm getRhythm( @PathVariable Long id ) 
	{
		Rhythm rhythm = repository.findById(id)
			      .orElseThrow(() -> new RhythmNotFoundException(id));
	
		return rhythm;
	}
	
    
	@GetMapping("/getRhythmsList")
	public List<Rhythm> getRhythm() 
	{
		return StreamSupport.stream(repository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
	}
	
	@DeleteMapping("/deleteRhythm/{id}")
	public void deleteRhythm( @PathVariable Long id ) 
	{
		repository.deleteById( id );
	}

}