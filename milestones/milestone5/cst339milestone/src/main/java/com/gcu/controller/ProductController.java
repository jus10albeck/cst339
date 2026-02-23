package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.CollectionsBusinessInterface;
import com.gcu.model.MovieModel;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController 
{
	private final CollectionsBusinessInterface collectionsService;
	public ProductController(CollectionsBusinessInterface collectionsService)
	{
		this.collectionsService = collectionsService;
	}
	
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("movie") MovieModel movie,
						BindingResult result, HttpSession session,
						RedirectAttributes ra, Model model)
	{
		if (session.getAttribute("USER") == null)
			return "redirect:/login/";
		
		if (result.hasErrors())
		{
			model.addAttribute("openCreateModal", true);
			String user = (String) session.getAttribute("USER");
			model.addAttribute("movies", collectionsService.getCollectionFor(user));
			model.addAttribute("title", "Collections");
			return "collections";
		}
		
		String user = (String) session.getAttribute("USER");
		collectionsService.addMovieFor(user, movie);
		
		ra.addFlashAttribute("message", "Product created!");
		return "redirect:/collections/";
	}
}
