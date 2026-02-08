package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.business.CollectionsBusinessInterface;
import com.gcu.model.MovieModel;

import jakarta.servlet.http.HttpSession;

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
}
