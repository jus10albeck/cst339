package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.CollectionsBusinessInterface;
import com.gcu.model.MovieModel;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/collections")
public class CollectionsController 
{
	private final CollectionsBusinessInterface service;

	public CollectionsController(CollectionsBusinessInterface service) 
	{
        this.service = service;
    }

	@GetMapping("/")
	public String display(Model model, HttpSession session)
	{
		model.addAttribute("title", "Collection");
		
		String user = (String) session.getAttribute("USER");
		
		if (user == null)
			return "redirect:/login/";
			
		model.addAttribute("movies", service.getCollectionFor(user));
		
		if (!model.containsAttribute("movie"))
			model.addAttribute("movie", new MovieModel());

		return "collections";
	}
	

	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("movie") MovieModel movie,
	                     BindingResult br,
	                     HttpSession session,
	                     RedirectAttributes ra) 
	{
	    String user = (String) session.getAttribute("USER");
	    if (user == null) return "redirect:/login/";
	
	    if (br.hasErrors()) 
	    {
	        ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
	        ra.addFlashAttribute("movie", movie);
	        ra.addFlashAttribute("openCreateModal", true);
	        return "redirect:/collections/";
	    }
	
	    try
	    {
	    	service.addMovieFor(user, movie);
	    }
	    catch (IllegalArgumentException dupEx)
	    {
	    	br.rejectValue("movieName", "duplicate", dupEx.getMessage());
	        ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
	        ra.addFlashAttribute("movie", movie);
	        ra.addFlashAttribute("openCreateModal", true);
	        return "redirect:/collections/";
	    }
	    
	    return "redirect:/collections/";
	}
	

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable("id") Long id,
	                       Model model,
	                       HttpSession session) {
	    String user = (String) session.getAttribute("USER");
	    if (user == null) return "redirect:/login/";
	
	    // Here’s a simple approach:
	    var rows = service.getCollectionFor(user); // small lists are fine
	    var row = rows.stream().filter(r -> r.getId().equals(id)).findFirst()
	                  .orElseThrow(() -> new IllegalStateException("Movie not found or not yours."));
	
	    var movie = new com.gcu.model.MovieModel(row.getId(), row.getMovieName(), row.getDirector(), row.getRating(), row.getVideoType());
	    model.addAttribute("movie", movie);
	    model.addAttribute("title", "Edit Movie");
	    return "collections-edit";
	}

	@PostMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id,
	                   @Valid @ModelAttribute("movie") MovieModel movie,
	                   BindingResult br,
	                   HttpSession session,
	                   RedirectAttributes ra) 
	{
	    String user = (String) session.getAttribute("USER");
	    if (user == null) return "redirect:/login/";
	
	    if (br.hasErrors()) 
	    {
	        ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
	        ra.addFlashAttribute("movie", movie);
	        ra.addFlashAttribute("openEditModal", true);
	        return "redirect:/collections/";
	    }
	
	    try 
	    {
	        service.updateMovieFor(user, id, movie);
	    } 
	    catch (IllegalArgumentException dupEx) 
	    {
	        br.rejectValue("movieName", "duplicate", dupEx.getMessage());
	        ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
	        ra.addFlashAttribute("movie", movie);
	        ra.addFlashAttribute("openEditModal", true);
	        return "redirect:/collections/";
	    }
	
	    return "redirect:/collections/";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id,
	                     HttpSession session,
	                     RedirectAttributes ra) {
	    String user = (String) session.getAttribute("USER");
	    if (user == null) return "redirect:/login/";
	
	    try {
	        service.deleteMovieFor(user, id);
	        ra.addFlashAttribute("success", "Movie deleted.");
	    } catch (Exception ex) {
	        ra.addFlashAttribute("error", "Could not delete movie: " + ex.getMessage());
	    }
	    return "redirect:/collections/";
	}

}
