package com.gcu.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.CollectionsBusinessInterface;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    private final CollectionsBusinessInterface service;

    /**
     * Constructor for WishlistController. It initializes the controller with 
     * a CollectionsBusinessInterface which is used to handle the business logic related to the user's wishlist. 
     * @param service
     */
    public WishlistController(CollectionsBusinessInterface service) 
    {
        this.service = service;
    }

    /**
     * Displays the wishlist view for the authenticated user. It retrieves the user's wishlist 
     * from the service and adds it to the model. It also sets the title attribute to "Wishlist". 
     * If the model does not already contain a "movie" attribute, it adds a new MovieModel to the 
     * model to be used in the create movie modal. Finally, it returns the view name "wishlist" to be rendered.   
     * @param auth
     * @param model
     * @return
     */
    @GetMapping({ "", "/" })
    public String display(Authentication auth, Model model) {
        model.addAttribute("title", "Wishlist");
        model.addAttribute("movies", service.getWishlistFor(auth.getName()));
        // Ensure the modal has a bound object so th:field works
        if (!model.containsAttribute("movie")) {
            model.addAttribute("movie", new com.gcu.model.MovieModel());
        }
        return "wishlist";
    }

    /**
     * Handles the creation of a new movie in the user's wishlist. It validates the input data and 
     * checks for any errors. If there are validation errors, it prepares the model to display the wishlist 
     * view with the appropriate error messages and flags to reopen the create movie modal. If the input is 
     * valid, it adds the new movie to the user's wishlist and redirects back to the wishlist view. If 
     * there is an attempt to add a duplicate movie, it catches the IllegalArgumentException and prepares the 
     * model to display the wishlist view with an error message indicating the duplicate entry.  
     * @param movie
     * @param br
     * @param auth
     * @param ra
     * @return
     */
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("movie") com.gcu.model.MovieModel movie,
                         BindingResult br,
                         Authentication auth,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            // Reopen the modal and keep validation messages
            ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
            ra.addFlashAttribute("movie", movie);
            ra.addFlashAttribute("openCreateModal", true);
            return "redirect:/wishlist/";
        }
        try {
            service.createMovieAndAddToWishlist(auth.getName(), movie);
        } catch (IllegalArgumentException ex) {
            br.rejectValue("movieName", "duplicate", ex.getMessage());
            ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
            ra.addFlashAttribute("movie", movie);
            ra.addFlashAttribute("openCreateModal", true);
            return "redirect:/wishlist/";
        }
        return "redirect:/wishlist/";
    }

    /**
     * Handles the addition of an existing movie to the user's wishlist. It takes the movie ID
     *  as a parameter and adds the corresponding movie to the user's wishlist. After adding the
     *  movie, it redirects back to the wishlist view.  
     * @param movieId
     * @param auth
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestParam("movieId") Long movieId, Authentication auth) 
    {
        service.addToWishlist(auth.getName(), movieId);
        return "redirect:/wishlist/";
    }

    /**
     * Handles the movement of a movie from the user's wishlist to their collection. 
     * It takes the movie ID and the desired format as parameters, moves the movie 
     * from the wishlist to the collection, and then redirects back to the wishlist view. 
     * @param movieId
     * @param format
     * @param auth
     * @return
     */
    @PostMapping("/{movieId}/move-to-collection")
    public String moveToCollection(@PathVariable("movieId") Long movieId,
                                   @RequestParam("format") String format,
                                   Authentication auth) 
    {
        service.moveWishlistItemToCollection(auth.getName(), movieId, format);
        return "redirect:/wishlist/";
    }

    /**
     * Handles the removal of a movie from the user's wishlist. It takes the movie ID 
     * as a parameter, removes the corresponding movie from the user's wishlist, and 
     * then redirects back to the wishlist view.   
     * @param movieId
     * @param auth
     * @return
     */
    @PostMapping("/{movieId}/remove")
    public String remove(@PathVariable("movieId") Long movieId, Authentication auth) 
    {
        service.removeFromWishlist(auth.getName(), movieId);
        return "redirect:/wishlist/";
    }
}