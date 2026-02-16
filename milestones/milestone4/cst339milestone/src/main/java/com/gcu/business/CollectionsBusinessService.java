package com.gcu.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.gcu.data.dao.MovieListDao;
import com.gcu.data.entity.CollectionEntity;
import com.gcu.data.entity.DirectorEntity;
import com.gcu.data.entity.MovieEntity;
import com.gcu.data.entity.RatingEntity;
import com.gcu.data.entity.VideoTypeEntity;
import com.gcu.data.repository.CollectionRepository;
import com.gcu.data.repository.DirectorRepository;
import com.gcu.data.repository.MovieListRow;
import com.gcu.data.repository.MovieRepository;
import com.gcu.data.repository.RatingRepository;
import com.gcu.data.repository.UserRepository;
import com.gcu.data.repository.VideoTypeRepository;
import com.gcu.model.MovieModel;

public class CollectionsBusinessService implements CollectionsBusinessInterface
{
	CollectionRepository collectionRepo;
	DirectorRepository directorRepo;
	RatingRepository ratingRepo;
	VideoTypeRepository videoTypeRepo;
	MovieRepository movieRepo;
	UserRepository userRepo;
	
	MovieListDao movieListDao;
	
	public CollectionsBusinessService(CollectionRepository collectionRepo,
			DirectorRepository directorRepo, RatingRepository ratingRepo,
			VideoTypeRepository videoTypeRepo, MovieRepository movieRepo,
			UserRepository userRepo, MovieListDao movieListDao)
	{
		this.collectionRepo = collectionRepo;
		this.directorRepo = directorRepo;
		this.ratingRepo = ratingRepo;
		this.videoTypeRepo = videoTypeRepo;
		this.movieRepo = movieRepo;
		this.userRepo = userRepo;
		this.movieListDao = movieListDao;
	}
	
	@Override
	public void test() 
	{
		System.out.println("Test message from CollectionsBusinessService");
	}
	
	// ----- READ -----
	@Override
	public List<MovieModel> getCollectionFor(String username) 
	{
		var rows = movieListDao.findMoviesForUsername(username);

		var result = new java.util.ArrayList<MovieModel>(rows.size());

		for (var r : rows) {
	        result.add(new MovieModel(
	            r.getId(),
	            r.getMovieName(),
	            r.getDirector(),
	            r.getRating(),
	            r.getVideoType()
	        ));
	    }
	    return result;

	}
	
	// ----- Create -----

	@Override
	@Transactional
	public void addMovieFor(String username, MovieModel movie) 
	{
		boolean dup = collectionRepo.existsMovieNameForUser(username, movie.getMovieName());
		if (dup)
			throw new IllegalArgumentException("This movie is already in your collection.");
		
	    // userId by username
	    Long userId = userRepo.findUserIdByUsername(username)
	            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
	
	    // Resolve or create Director (split "First Last") – with final vars for lambda
	    final String full = movie.getDirector() != null ? movie.getDirector().trim() : "";
	    final int idx = full.lastIndexOf(' ');
	    final String firstName = (idx > 0) ? full.substring(0, idx).trim() : full;
	    final String lastName  = (idx > 0) ? full.substring(idx + 1).trim() : "";
	
	    DirectorEntity director = directorRepo
	            .findByDirectorFirstNameAndDirectorLastName(firstName, lastName)
	            .orElseGet(() -> {
	                DirectorEntity d = new DirectorEntity();
	                d.setDirectorFirstName(firstName);
	                d.setDirectorLastName(lastName);
	                return directorRepo.save(d);
	            });
	
	    // Rating: lookup-only (fail if invalid)
	    String ratingStr = movie.getRating() != null ? movie.getRating().trim() : "";
	    RatingEntity rating = ratingRepo.findByRating(ratingStr)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid rating: " + ratingStr));
	
	    // Video Type: normalize UI value to DB canonical, then lookup-only (fail if invalid)
	    String uiType = movie.getVideoType() != null ? movie.getVideoType().trim() : "";
	    String canonicalType = normalizeVideoType(uiType); // e.g., "Blu-Ray" -> "Blu-ray"
	    VideoTypeEntity vt = videoTypeRepo.findByType(canonicalType)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid video type: " + uiType));
	
	    // Insert the Movie
	    MovieEntity m = new MovieEntity();
	    m.setMovieName(movie.getMovieName());
	    m.setDirectorId(director.getId());
	    m.setRatingId(rating.getId());
	    m.setVideoTypeId(vt.getId());
	    MovieEntity saved = movieRepo.save(m);
	
	    // Map to the user's collection
	    CollectionEntity link = new CollectionEntity();
	    link.setUserId(userId);
	    link.setMovieId(saved.getId());
	    collectionRepo.save(link);
	}
	
	private static String normalizeVideoType(String uiType) 
	{
	    if (uiType == null) return "";
	    String t = uiType.trim();
	    if (t.equalsIgnoreCase("Blu-Ray")) return "Blu-ray"; 
	    if (t.equalsIgnoreCase("DVD"))     return "DVD";
	    if (t.equalsIgnoreCase("VHS"))     return "VHS";
	    return t; 
	}

	
	public void init()
	{
		System.out.println("Test Init");
	}

	@Override
	public void destroy() 
	{
		System.out.println("Test Destroy");
	}

	@Override
	public void updateMovieFor(String username, Long movieId, MovieModel updated)
	{
		if (!collectionRepo.userOwnsMovie(username, movieId))
			throw new IllegalStateException("Not authorized to edit this movie.");
		
		
		final String full = updated.getDirector() != null ? updated.getDirector().trim() : "";
	    final int idx = full.lastIndexOf(' ');
	    final String firstName = (idx > 0) ? full.substring(0, idx).trim() : full;
	    final String lastName  = (idx > 0) ? full.substring(idx + 1).trim() : "";
	
	    var director = directorRepo
	        .findByDirectorFirstNameAndDirectorLastName(firstName, lastName)
	        .orElseGet(() -> 
	        {
	            var d = new com.gcu.data.entity.DirectorEntity();
	            d.setDirectorFirstName(firstName);
	            d.setDirectorLastName(lastName);
	            return directorRepo.save(d);
	        });
	    

	    var rating = ratingRepo.findByRating(updated.getRating())
	    		.orElseThrow(() -> new IllegalArgumentException("Invalid rating: " + updated.getRating()));

	    // Lookup video type (normalize then lookup)
	    String canonicalType = normalizeVideoType(updated.getVideoType());
	    var vt = videoTypeRepo.findByType(canonicalType)
	        .orElseThrow(() -> new IllegalArgumentException("Invalid video type: " + updated.getVideoType()));
	
	    // Load and update MovieEntity
	    var movieOpt = movieRepo.findById(movieId);
	    
	    if (movieOpt.isEmpty()) 
	        throw new IllegalStateException("Movie not found.");
	    
	    var m = movieOpt.get();
	    m.setMovieName(updated.getMovieName());
	    m.setDirectorId(director.getId());
	    m.setRatingId(rating.getId());
	    m.setVideoTypeId(vt.getId());
	    movieRepo.save(m);
	}

	@Override
	public void deleteMovieFor(String username, Long movieId)
	{

		if (!collectionRepo.userOwnsMovie(username, movieId)) 
			throw new IllegalStateException("Not authorized to delete this movie.");
   

	    // Remove the link from user's collection
	    collectionRepo.deleteFromUserCollection(username, movieId);
	
	    // If no other users reference this movie, delete it
	    int refs = collectionRepo.countCollectionsForMovie(movieId);
	    if (refs == 0) {
	        movieRepo.deleteById(movieId);
        // Optional cleanup: delete orphan directors if desired (skip for now)
    }

		
	}

	
}
