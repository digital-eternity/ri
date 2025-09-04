package com.ri.api;

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

import com.ri.controller.RhythmNotFoundException;
import com.ri.dao.RhythmRepository;
import com.ri.model.rhythm.Rhythm;

/**
 * Rhythm Rest API controller
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/rhythm")
@Validated
public class RhythmController 
{
    private final RhythmRepository repository; 

    public RhythmController( RhythmRepository rep ) 
    { 
    	this.repository = rep;
    }
	
    @PostMapping("/add")
    public Rhythm addRhythm( @RequestBody Rhythm newRhythm ) 
	{
		return repository.save( newRhythm );
	}
    
    //TODO: add validation and fix updating
    @PutMapping("/update/{id}")
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
	
    @GetMapping("/get/{id}")
    @CrossOrigin(origins="http://localhost:8081")
	public Rhythm getRhythm( @PathVariable Long id ) 
	{
		Rhythm rhythm = repository.findById(id)
			      .orElseThrow(() -> new RhythmNotFoundException(id));
	
		return rhythm;
	}
	
    
	@GetMapping("/getAll")
	public List<Rhythm> getRhythms() 
	{
		return StreamSupport.stream(repository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteRhythm( @PathVariable Long id ) 
	{
		repository.deleteById( id );
	}

}