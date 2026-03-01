package com.gcu.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.CollectionsBusinessInterface;
import com.gcu.model.MovieModel;

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
                         BindingResult result,
                         Authentication auth,
                         RedirectAttributes ra,
                         Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("openCreateModal", true);
            String username = auth.getName();
            model.addAttribute("movies", collectionsService.getCollectionFor(username));
            model.addAttribute("title", "Collections");
            return "collections";
        }

        String username = auth.getName();
        collectionsService.addMovieFor(username, movie);

        ra.addFlashAttribute("message", "Product created!");
        return "redirect:/collections/";
    }
}
