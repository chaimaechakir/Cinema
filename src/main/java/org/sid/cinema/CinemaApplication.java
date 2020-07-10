package org.sid.cinema;

import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Salle;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.service.ICinemaInitservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner{
@Autowired
private RepositoryRestConfiguration restConfiguration;

	@Autowired
	private ICinemaInitservice cinemaInitservice;
	
	public static void main(String[] args) {
		SpringApplication.run(CinemaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		restConfiguration.exposeIdsFor(Film.class, Salle.class,Ticket.class);
		cinemaInitservice.initVilles();
		cinemaInitservice.initCinemas();
		cinemaInitservice.initSalles();
		cinemaInitservice.initPlaces();
		cinemaInitservice.initSeances();
		cinemaInitservice.initCategories();
		cinemaInitservice.initFilms();
		cinemaInitservice.initProjections();
		cinemaInitservice.initTickets();
	}
}
