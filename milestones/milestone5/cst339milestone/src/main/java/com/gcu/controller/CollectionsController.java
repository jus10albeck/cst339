package com.gcu.controller;

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

	@GetMapping({"", "/"})
	public String display(@RequestParam(value = "albumId", required = false)
							Long albumId, Model model, HttpSession session)
	{
		 String user = (String) session.getAttribute("USER");
		    if (user == null) return "redirect:/login/"; 

		    var albums = service.getAlbumsFor(user);

		    Long currentAlbumId = albumId;
		    if (currentAlbumId == null) {
		        if (albums.isEmpty()) {
		            // Default main library = "My Collection"
		            Long newId = service.createAlbumFor(user, "My Collection", null);
		            currentAlbumId = newId;
		            albums = service.getAlbumsFor(user); // refresh list after create
		        } else {
		            // Prefer "My Collection" if it exists; otherwise first album
		            var preferred = albums.stream()
		                                  .filter(a -> "My Collection".equalsIgnoreCase(a.getName()))
		                                  .findFirst()
		                                  .orElse(albums.get(0));
		            currentAlbumId = preferred.getId();
		        }
		    }

		    model.addAttribute("movies", service.getCollectionFor(user, currentAlbumId));

		    // existing movie backing object
		    if (!model.containsAttribute("movie"))
		        model.addAttribute("movie", new com.gcu.model.MovieModel());

		    // album backing object for the Create Album modal
		    if (!model.containsAttribute("album"))
		        model.addAttribute("album", new com.gcu.model.AlbumModel());

		    model.addAttribute("albums", albums);
		    model.addAttribute("currentAlbumId", currentAlbumId);
		    model.addAttribute("title", "Collection");
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
						 @RequestParam("albumId") Long albumId,
	                     HttpSession session,
	                     RedirectAttributes ra) 
	{
	    String user = (String) session.getAttribute("USER");
	    if (user == null) return "redirect:/login/";
	
	    try 
	    {
	        service.deleteMovieFor(user, albumId, id);
	        ra.addFlashAttribute("success", "Removed from album");
	    } 
	    catch (Exception ex) 
	    {
	        ra.addFlashAttribute("error", "Could not delete movie: " + ex.getMessage());
	    }
	    return "redirect:/collections/?albumId=" + albumId;
	}
	
	@PostMapping("/albums/create")
	public String createAlbum(@Valid @ModelAttribute("album") AlbumModel album,
							  BindingResult br, HttpSession session,
							  RedirectAttributes ra)
	{
		String user = (String) session.getAttribute("USER");
		if (user == null)
			return "redirect:/login/";
		
		if (br.hasErrors())
		{
			ra.addFlashAttribute("org.springframework.validation.BindingResult.album", br);
			ra.addFlashAttribute("album", album);
			ra.addFlashAttribute("openCreateAlbumModal", true);
			return "redirect:/collections/";
		}
		
		Long id = service.createAlbumFor(user, album.getName(), album.getDescription());
		ra.addFlashAttribute("message", "Album created!");
		return "redirect:/collections/?albumId=" + id;
	}
	
	@PostMapping("/add-to-album")
	public String addToAlbum(@RequestParam("movieId") Long movieId,
							 @RequestParam("albumId") Long albumId,
							 @RequestParam("fromAlbumId") Long fromAlbumId,
							 HttpSession session,
							 RedirectAttributes ra)
	{
		String user = (String) session.getAttribute("USER");
		if (user == null)
			return "redirect:/login/";
		
		service.addMovieToAlbum(user, albumId, movieId);
		ra.addFlashAttribute("success", "Movie added to album.");
		
		return "redirect:/collections/?albumId=" + fromAlbumId;
		
	}
}
