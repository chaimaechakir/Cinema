package org.sid.cinema.web;

import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VilleController {

	@Autowired
	CinemaRepository cinemaRepository;
	@Autowired
	VilleRepository villeRepository;
	
	@GetMapping(path = "/ville")
	public String villes(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		Page<Ville> villes = villeRepository.findByNameContains(keyword, PageRequest.of(page, size));
		model.addAttribute("villes", villes.getContent());
		model.addAttribute("pages", new int[villes.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", keyword);
		return "ville";
	}

}
