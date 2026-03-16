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
    /**
     * The ProductController class is responsible for handling requests related 
     * to product management. It interacts with the CollectionsBusinessInterface 
     * to perform operations on the user's movie collection. The controller 
     * provides functionality to create new movies in the collection, ensuring that the 
     * input is valid and that the user is authenticated before allowing any modifications to their collection.    
     */
	private final CollectionsBusinessInterface collectionsService;
	public ProductController(CollectionsBusinessInterface collectionsService)
	{
		this.collectionsService = collectionsService;
	}
	
    /**
     * Handles the creation of a new movie in the user's collection. It validates the input data and 
     * checks for any errors. If there are validation errors, it prepares the model to display the collection 
     * view with the appropriate error messages and flags to reopen the create movie modal. If the input is 
     * valid, it adds the new movie to the user's collection and redirects back to the collections view with a success message.   
     */
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
