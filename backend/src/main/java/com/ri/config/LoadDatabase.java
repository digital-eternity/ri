package com.ri.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ri.converter.JSONRhythmConverter;
import com.ri.dao.RhythmRepository;
import com.ri.model.rhythm.Rhythm;


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
			
			//TODO: update with 
			// save a few rhythms
			log.info("Preloading...");
			Rhythm rhythm = new Rhythm( 8 );
			rhythm.setNote( 0 );
			rhythm.setNote( 3 );
			rhythm.setNote( 6 );
			
			Rhythm rhythm2 = new Rhythm( 9 );
			rhythm2.setNote( 0 );
			rhythm2.setNote( 3 );
			rhythm2.setNote( 6 );
			
			log.info("Loading rhythm1 " + repository.save( rhythm ) );
			log.info("Loading rhythm2 " + repository.save( rhythm2 ) );
			
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
