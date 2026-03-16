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
import com.gcu.model.AlbumModel;
import com.gcu.model.MovieModel;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/collections")
public class CollectionsController 
{
	private final CollectionsBusinessInterface service;

	/**
	 * Constructor with dependency injection for the business service.
	 * @param service
	 */
	public CollectionsController(CollectionsBusinessInterface service) 
	{
        this.service = service;
    }

	/**
	 * Displays the user's movie collection. If an albumId is provided, it shows that album;
	 * otherwise, it defaults to "My Collection" or the first album. It also prepares the model with necessary attributes for the view.
	 * @param albumId
	 * @param model
	 * @param auth
	 * @return
	 */
	@GetMapping({"", "/"})
	public String display(@RequestParam(value = "albumId", required = false)
							Long albumId, Model model, Authentication auth)
	{
		String username = auth.getName();

	    var albums = service.getAlbumsFor(username);
	    Long currentAlbumId = albumId;
	    if (currentAlbumId == null) 
	    {
	        if (albums.isEmpty()) 
	        {
	            Long newId = service.createAlbumFor(username, "My Collection", null);
	            currentAlbumId = newId;
	            albums = service.getAlbumsFor(username);
	        } 
	        else 
	        {
	            var preferred = albums.stream()
	                .filter(a -> "My Collection".equalsIgnoreCase(a.getName()))
	                .findFirst()
	                .orElse(albums.get(0));
	            currentAlbumId = preferred.getId();
	        }
	    }
	
	    model.addAttribute("movies", service.getCollectionFor(username, currentAlbumId));
	    if (!model.containsAttribute("movie"))
	    	model.addAttribute("movie", new com.gcu.model.MovieModel());
	    
	    if (!model.containsAttribute("album")) 
	    	model.addAttribute("album", new com.gcu.model.AlbumModel());
	    
	    model.addAttribute("albums", albums);
	    model.addAttribute("currentAlbumId", currentAlbumId);
	    model.addAttribute("title", "Collection");
	    
	    return "collections";
	}

	/**
	 * Handles the creation of a new movie in the user's collection. It validates the input,
	 * checks for duplicates, and adds the movie to the specified album. If there are validation 
	 * errors or duplicates, it redirects back to the collection view with appropriate error 
	 * messages and flags to reopen the creation modal.
	 * @param movie
	 * @param br
	 * @param auth
	 * @param ra
	 * @return
	 */
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("movie") MovieModel movie,
	                     BindingResult br,
	                     Authentication auth,
	                     RedirectAttributes ra) 
	{
		String username = auth.getName();
	    if (username == null) return "redirect:/login/";
	
	    if (br.hasErrors()) 
	    {
	        ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
	        ra.addFlashAttribute("movie", movie);
	        ra.addFlashAttribute("openCreateModal", true);
	        return "redirect:/collections/";
	    }
	
	    try
	    {
	    	service.addMovieFor(username, movie);
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
	
	/**
	 * Displays the edit form for a specific movie. It checks if the movie belongs to the user and 
	 * prepares the model with the movie details for editing. If the movie is not found or 
	 * does not belong to the user, it throws an exception.
	 * @param id
	 * @param model
	 * @param auth
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable("id") Long id,
	                       Model model,
	                       Authentication auth) {
		String username = auth.getName();
	    if (username == null) return "redirect:/login/";
	
	    // Here’s a simple approach:
	    var rows = service.getCollectionFor(username); // small lists are fine
	    var row = rows.stream().filter(r -> r.getId().equals(id)).findFirst()
	                  .orElseThrow(() -> new IllegalStateException("Movie not found or not yours."));
	
	    var movie = new com.gcu.model.MovieModel(row.getId(), row.getMovieName(), row.getDirector(), row.getRating(), row.getVideoType());
	    model.addAttribute("movie", movie);
	    model.addAttribute("title", "Edit Movie");
	    return "collections-edit";
	}

	/**
	 * Handles the submission of the edit form for a movie. It validates the input, checks for duplicates, 
	 * and updates the movie details in the user's collection. If there are validation errors or duplicates, 
	 * it redirects back to the collection view with appropriate error messages and flags to reopen the edit modal.
	 * @param id
	 * @param movie
	 * @param br
	 * @param auth
	 * @param ra
	 * @return
	 */
	@PostMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id,
	                   @Valid @ModelAttribute("movie") MovieModel movie,
	                   BindingResult br,
	                   Authentication auth,
	                   RedirectAttributes ra) 
	{
		String username = auth.getName();
	    if (username == null) return "redirect:/login/";
	
	    if (br.hasErrors()) 
	    {
	        ra.addFlashAttribute("org.springframework.validation.BindingResult.movie", br);
	        ra.addFlashAttribute("movie", movie);
	        ra.addFlashAttribute("openEditModal", true);
	        return "redirect:/collections/";
	    }
	
	    try 
	    {
	        service.updateMovieFor(username, id, movie);
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
	
	/**
	 * Handles the deletion of a movie from the user's collection. It checks if the movie belongs to the 
	 * user and deletes it from the specified album. If the deletion is successful, it adds a 
	 * success message; otherwise it adds an error message.
	 * @param id
	 * @param albumId
	 * @param auth
	 * @param ra
	 * @return
	 */
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id,
						 @RequestParam("albumId") Long albumId,
	                     Authentication auth,
	                     RedirectAttributes ra) 
	{
		String username = auth.getName();
	    if (username == null) return "redirect:/login/";
	
	    try 
	    {
	        service.deleteMovieFor(username, albumId, id);
	        ra.addFlashAttribute("success", "Removed from album");
	    } 
	    catch (Exception ex) 
	    {
	        ra.addFlashAttribute("error", "Could not delete movie: " + ex.getMessage());
	    }
	    return "redirect:/collections/?albumId=" + albumId;
	}
	
	/**
	 * Handles the creation of a new album for the user. It validates the input and creates 
	 * a new album with the specified name and description. If there are validation errors, it 
	 * redirects back to the collection view with appropriate error messages and flags to reopen the album creation modal.	
	 * @param album
	 * @param br
	 * @param auth
	 * @param ra
	 * @return
	 */
	@PostMapping("/albums/create")
	public String createAlbum(@Valid @ModelAttribute("album") AlbumModel album,
							  BindingResult br, Authentication auth,
							  RedirectAttributes ra)
	{
		String username = auth.getName();
		if (username == null)
			return "redirect:/login/";
		
		if (br.hasErrors())
		{
			ra.addFlashAttribute("org.springframework.validation.BindingResult.album", br);
			ra.addFlashAttribute("album", album);
			ra.addFlashAttribute("openCreateAlbumModal", true);
			return "redirect:/collections/";
		}
		
		Long id = service.createAlbumFor(username, album.getName(), album.getDescription());
		ra.addFlashAttribute("message", "Album created!");
		return "redirect:/collections/?albumId=" + id;
	}
	
	/**
	 * Handles adding an existing movie to another album. 
	 * It checks if the movie belongs to the user and adds it to the specified album. 
	 * If the operation is successful, it adds a success message; otherwise it adds an error message. 
	 * After the operation, it redirects back to the collection view of the original album.
	 * @param movieId
	 * @param albumId
	 * @param fromAlbumId
	 * @param auth
	 * @param ra
	 * @return
	 */
	@PostMapping("/add-to-album")
	public String addToAlbum(@RequestParam("movieId") Long movieId,
	                         @RequestParam("albumId") Long albumId,
	                         @RequestParam("fromAlbumId") Long fromAlbumId,
	                         Authentication auth,
	                         RedirectAttributes ra)
	{

	    String username = auth.getName();

	    service.addMovieToAlbum(username, albumId, movieId);
	
	    ra.addFlashAttribute("success", "Movie added to album.");
	    return "redirect:/collections/?albumId=" + fromAlbumId;
	}
	
	/**
	 * Handles the deletion of an album from the user's collection. It checks if the album 
	 * belongs to the user and deletes it. If the deletion is successful, it adds a 
	 * success message; otherwise it adds an error message. After the operation, it redirects 
	 * back to the main collection view. Note that "My Collection" cannot be deleted, and 
	 * attempts to delete it will result in an error message. Additionally, if the album does 
	 * not belong to the user or is invalid, it will also result in an error message.	
	 * @param albumId
	 * @param auth
	 * @param ra
	 * @return
	 */
	@PostMapping("/albums/delete")
	public String deleteAlbum(@RequestParam("albumId") Long albumId,
	                          Authentication auth,
	                          RedirectAttributes ra)
	{
	    String username = auth.getName();
	    try 
	    {
	        service.deleteAlbumFor(username, albumId);
	        ra.addFlashAttribute("success", "Album deleted.");
	    } catch (IllegalArgumentException ex) {
	        // Includes attempts to delete "My Collection" or a non-owned/invalid album
	        ra.addFlashAttribute("error", ex.getMessage());
	    } catch (Exception ex) {
	        ra.addFlashAttribute("error", "Could not delete album: " + ex.getMessage());
	    }
	    return "redirect:/collections/";
	}
}
