package com.ri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ri.converter.JSONRhythmConverter;
import com.ri.dao.RhythmRepository;
import com.ri.model.Rhythm;


/**
 * Loads to DB all predefined rhythms
 */
@SpringBootApplication
public class LoadDatabase 
{
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	public static void main( String[] args )
	{
		SpringApplication.run(LoadDatabase.class, args);
	}
	

	@Bean
	CommandLineRunner initDatabase( RhythmRepository repository ) 
	{
		return (args) -> {
			
			// save a few rhythms
			log.info("Preloading...");
			Rhythm rhythm = new Rhythm( 8 );
			rhythm.setNote( 0 );
			rhythm.setNote( 2 );
			rhythm.setNote( 5 );
			
			log.info("Preloading " + repository.save( rhythm ) );
			
			
		    log.info( "Loaded .." + repository.count() + " rhythms" );
			
			// fetch all rhythms
			log.info("Rythms found with findAll():");
			log.info("-------------------------------");
			repository.findAll().forEach(ry -> {
				
				log.info(ry.toString());});
				log.info("");
			};
	}
}
