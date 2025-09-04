package com.ri.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ri.dao.RhythmRepository;
import com.ri.model.rhythm.Rhythm;

@Controller
@Validated
public class TestingController 
{
    private final RhythmRepository repository; 

    public TestingController(RhythmRepository rep) 
    { 
    	this.repository = rep;
    }
    

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", 
											required=false, 
											defaultValue="World") 
								String name, 
								Model model) 
	{
		Rhythm r = new Rhythm( 8 );
		
		Rhythm rhythm = repository.save( r );
		//Rhythm rhythm = repository.findById( 1L );
		
		model.addAttribute("rhythm", rhythm.toString());
		
		return "greeting";
	}

}