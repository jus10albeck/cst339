package com.gcu.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.gcu.data.dao.MovieListDao;
import com.gcu.data.entity.AlbumEntity;
import com.gcu.data.entity.AlbumMovieEntity;
import com.gcu.data.entity.DirectorEntity;
import com.gcu.data.entity.MovieEntity;
import com.gcu.data.entity.RatingEntity;
import com.gcu.data.entity.VideoTypeEntity;
import com.gcu.data.repository.AlbumMovieRepository;
import com.gcu.data.repository.AlbumRepository;
import com.gcu.data.repository.DirectorRepository;
import com.gcu.data.repository.MovieRepository;
import com.gcu.data.repository.RatingRepository;
import com.gcu.data.repository.UserRepository;
import com.gcu.data.repository.VideoTypeRepository;
import com.gcu.model.AlbumModel;
import com.gcu.model.MovieModel;

public class CollectionsBusinessService implements CollectionsBusinessInterface
{
	AlbumRepository albumRepo;
	AlbumMovieRepository albumMovieRepo;
	DirectorRepository directorRepo;
	RatingRepository ratingRepo;
	VideoTypeRepository videoTypeRepo;
	MovieRepository movieRepo;
	UserRepository userRepo;
	
	MovieListDao movieListDao;
	
	public CollectionsBusinessService(AlbumRepository albumRepo, 
			AlbumMovieRepository albumMovieRepo, DirectorRepository directorRepo,
			RatingRepository ratingRepo, VideoTypeRepository videoTypeRepo,
			MovieRepository movieRepo, UserRepository userRepo, MovieListDao movieListDao)
	{
		this.albumRepo = albumRepo;
		this.albumMovieRepo = albumMovieRepo;
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
		Long userId = userRepo.findUserIdByUsername(username)
				.orElseThrow(() -> new IllegalStateException("User not found: " + username));
		Long albumId = ensurePrimaryAlbumId(username, userId);
		var rows = movieListDao.findMoviesForUsername(username, albumId);

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
		Long userId = userRepo.findUserIdByUsername(username).orElseThrow();
		Long albumId = ensurePrimaryAlbumId(username, userId);
		
		boolean dup = albumMovieRepo.existsMovieNameInAlbum(username, albumId, movie.getMovieName());
		if (dup)
			throw new IllegalArgumentException("This movie is already in your collection.");
	
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
	
	    var saved = movieRepo.save(m);
	    
	    var link = new AlbumMovieEntity();
	    link.setAlbumId(albumId);
	    link.setMovieId(saved.getId());
	    link.setAddedTime(java.time.LocalDateTime.now());
	    albumMovieRepo.save(link);
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
		if (!albumMovieRepo.userOwnsMovie(username, movieId))
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
	public void deleteMovieFor(String username, Long albumId, Long movieId)
	{
		if (!albumMovieRepo.userOwnsMovie(username, movieId))
			throw new IllegalStateException("Not authorized to delete this movie.");


	    Long userId  = userRepo.findUserIdByUsername(username)
	            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
	    
	    Long primary = ensurePrimaryAlbumId(username, userId);
	
	    // If current album is NOT the primary album, only unlink from that album
	    if (!primary.equals(albumId)) 
	    {
	        albumMovieRepo.deleteFromUserAlbum(username, albumId, movieId);
	        return;
	    }
	
	    // Else: we’re in "My Collection" — keep your existing, full delete behavior
	    albumMovieRepo.deleteFromUserAlbum(username, albumId, movieId);
	    
	    int refs = albumMovieRepo.countAlbumLinksForMovie(movieId);
	    
	    if (refs == 0) 
	        movieRepo.deleteById(movieId);
	}
	

	private Long ensurePrimaryAlbumId(String username, Long userId) {
    return albumRepo.findPrimaryAlbumIdByUsername(username)
        .orElseGet(() -> {
            var a = new com.gcu.data.entity.AlbumEntity();
            a.setUserId(userId);
            a.setName("My Collection");
            a.setDescription(null);
            a.setUpdatedTime(java.time.LocalDateTime.now());
            return albumRepo.save(a).getId();
        });
}

	@Override
	public List<AlbumModel> getAlbumsFor(String username)
	{
		var list = albumRepo.findAllForUsername(username);
		var out = new ArrayList<AlbumModel>(list.size());
		
		for (var a : list) 
			out.add(new AlbumModel(a.getId(), a.getName(), a.getDescription()));
		return out;
	}

	@Override
	public Long createAlbumFor(String username, String name, String description)
	{
		Long userId = userRepo.findUserIdByUsername(username).orElseThrow(() -> new IllegalStateException("User not found: " + username));
		var a = new AlbumEntity();
		a.setUserId(userId);
		a.setName(name);
		a.setDescription(description);
		a.setUpdatedTime(java.time.LocalDateTime.now());
		return albumRepo.save(a).getId();
	}

	@Override
	public void addMovieToAlbum(String username, Long albumId, Long movieId)
	{
		albumMovieRepo.addMovieToAlbum(albumId, movieId);
	}

	@Override
	public List<MovieModel> getCollectionFor(String username, Long albumId)
	{
		var rows = movieListDao.findMoviesForUsername(username, albumId);
	    var result = new java.util.ArrayList<MovieModel>(rows.size());
	    
	    for (var r : rows)
	        result.add(new MovieModel(r.getId(), r.getMovieName(), r.getDirector(), r.getRating(), r.getVideoType()));

		return result;
	}

}
