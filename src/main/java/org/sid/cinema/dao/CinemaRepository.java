package org.sid.cinema.dao;

import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


@RepositoryRestResource 
@CrossOrigin("*")
public interface CinemaRepository extends JpaRepository<Cinema,Long> {
	public Page<Cinema> findByNameContains(String keyword ,Pageable pageable);
	public Page<Cinema> findByVilleAndNameContains(Ville ville, String keyword, Pageable pageable);
}

