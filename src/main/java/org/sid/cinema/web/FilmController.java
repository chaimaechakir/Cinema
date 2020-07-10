package org.sid.cinema.web;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Valid;

import org.sid.cinema.dao.CategorieRepository;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@CrossOrigin("*")
public class FilmController {

	@Autowired
	CinemaRepository cinemaRepository;
	@Autowired
	VilleRepository villeRepository;
	@Autowired
	FilmRepository filmRepository;
	@Autowired
	CategorieRepository categorieRepository;

	@GetMapping(path = "/film")
	public String film(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) throws IOException {
		Page<Film> pagefilms = filmRepository.findByTitreContains(keyword, PageRequest.of(page, size));
		model.addAttribute("pagefilms", pagefilms.getContent());
		model.addAttribute("currentpage", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("size", size);
		model.addAttribute("pages", new int[pagefilms.getTotalPages()]);
		return "film";
	}

	@GetMapping(path = "/supprimerFilm")
	public String supprimerfilm(Long id, String keyword, int page, int size, Model model) throws IOException {
		Film f = filmRepository.findById(id).get();
		model.addAttribute("id_courant", id);
		if (f.getProjections().isEmpty()) {
			filmRepository.deleteById(id);
			model.addAttribute("modeSup", "Autorise");
		} else {
			model.addAttribute("modeSup", "nonAutorise");
		}
		return film(model, page, size, keyword);
	}

	@GetMapping(path = "/formFilm")
	public String formFilm(Model model) {
		List<Categorie> categories = categorieRepository.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("film", new Film());
		model.addAttribute("mode", "nouveau");
		return "formFilm";
	}

	@PostMapping(path = "/enregistrerFilm")
	public String enregistrerFilm(@Valid Film f, BindingResult bindingResult, Model model,MultipartFile file) throws IOException {

		String UPLOADED_FOLDER = System.getProperty("user.dir") + "/images/";
		 byte[] bytes = file.getBytes();
         Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
         Files.write(path, bytes);
         f.setPhoto(file.getOriginalFilename());
		if (bindingResult.hasErrors())
			return "formFilm";
		filmRepository.save(f);
		model.addAttribute("film", f);
		return "validationFilm";
	}

	@GetMapping(path = "/modifierFilm")
	public String modifierFilm(Model model, Long id) {
		List<Categorie> categories = categorieRepository.findAll();
		model.addAttribute("categories", categories);

		Film f = filmRepository.findById(id).get();
		model.addAttribute("film", f);
		model.addAttribute("mode", "modifier");
		return "formFilm";
	}

}
