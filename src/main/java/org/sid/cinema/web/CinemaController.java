package org.sid.cinema.web;

import java.util.List;
import javax.validation.Valid;

import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CinemaController {

	@Autowired
	CinemaRepository cinemaRepository;
	@Autowired
	VilleRepository villeRepository;

	@GetMapping(path = "/")
	public String index() {
		
		return "index";
	}
	
	@GetMapping(path = "/cinema")
	public String cinemas(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "ville", defaultValue = "1") Long ville,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		Ville currentVille = villeRepository.findById(ville).get();
		Page<Cinema> pagecinemas = cinemaRepository.findByVilleAndNameContains(currentVille, keyword, PageRequest.of(page, size));
		model.addAttribute("pagecinemas", pagecinemas.getContent());
		model.addAttribute("currentpage", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("size", size);
		model.addAttribute("ville", ville);
		model.addAttribute("pages", new int[pagecinemas.getTotalPages()]);
		return "cinema";
	}

	@GetMapping(path = "/supprimerCinema")
	public String supprimercinema(Long id, String keyword,Long ville, int page, int size, Model model) {
		Cinema c = cinemaRepository.findById(id).get();
		model.addAttribute("id_courant", id);
		if (c.getSalles().isEmpty()) {
			cinemaRepository.deleteById(id);
			model.addAttribute("modeSup", "Autorise");
		} else {
			model.addAttribute("modeSup", "nonAutorise");
		}
		return "redirect:/cinema?keyword=" + keyword + "&ville=" + ville;
	}

	@GetMapping(path = "/formCinema")
	public String formCinema(Model model) {
		List<Ville> villes = villeRepository.findAll();
		model.addAttribute("villes", villes);
		model.addAttribute("cinema", new Cinema());
		model.addAttribute("mode", "nouveau");
		return "formCinema";
	}

	@PostMapping(path = "/enregistrerCinemas")
	public String enregistrerCinema(@Valid Cinema c, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors())
		{
			List<Ville> villes = villeRepository.findAll();
			model.addAttribute("villes", villes);
			model.addAttribute("mode", "nouveau");
			return "formCinema";
		}
		cinemaRepository.save(c);
		model.addAttribute("cinema", c);
		return "validationCinema";
	}

	@GetMapping(path = "/modifierCinema")
	public String modifierCinema(Model model, Long id) {
		List<Ville> villes = villeRepository.findAll();
		model.addAttribute("villes", villes);

		Cinema c = cinemaRepository.findById(id).get();
		model.addAttribute("cinema", c);
		model.addAttribute("mode", "modifier");
		return "formCinema";
	}
}
