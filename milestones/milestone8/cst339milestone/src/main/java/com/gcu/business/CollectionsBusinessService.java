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
import com.gcu.data.repository.WishlistRepository;
import com.gcu.data.repository.WishlistRow;
import com.gcu.model.AlbumModel;
import com.gcu.model.MovieModel;
import com.gcu.security.CryptoService;

public class CollectionsBusinessService implements CollectionsBusinessInterface
{
    private final AlbumRepository albumRepo;
    private final AlbumMovieRepository albumMovieRepo;
    private final DirectorRepository directorRepo;
    private final RatingRepository ratingRepo;
    private final VideoTypeRepository videoTypeRepo;
    private final MovieRepository movieRepo;
    private final UserRepository userRepo;
    private final MovieListDao movieListDao;
    private final CryptoService crypto;
    private final WishlistRepository wishlistRepo;

    /**
     * Constructor for CollectionsBusinessService
     * @param albumRepo
     * @param albumMovieRepo
     * @param directorRepo
     * @param ratingRepo
     * @param videoTypeRepo
     * @param movieRepo
     * @param userRepo
     * @param movieListDao
     * @param crypto
     * @param wishlistRepo
     */
    public CollectionsBusinessService(AlbumRepository albumRepo,
                                      AlbumMovieRepository albumMovieRepo,
                                      DirectorRepository directorRepo,
                                      RatingRepository ratingRepo,
                                      VideoTypeRepository videoTypeRepo,
                                      MovieRepository movieRepo,
                                      UserRepository userRepo,
                                      MovieListDao movieListDao,
                                      CryptoService crypto, 
                                      WishlistRepository wishlistRepo)
    {
        this.albumRepo = albumRepo;
        this.albumMovieRepo = albumMovieRepo;
        this.directorRepo = directorRepo;
        this.ratingRepo = ratingRepo;
        this.videoTypeRepo = videoTypeRepo;
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
        this.movieListDao = movieListDao;
        this.crypto = crypto;
        this.wishlistRepo = wishlistRepo;
    }

    /**
     * 
     * @param username
     * @return
     */
    private byte[] hashOf(String username) 
    {
        return crypto.usernameHash(crypto.normalizeUsername(username));
    }

    /**
     * Returns the movies for the specified album belonging to the current user
     */
    @Override
    public List<MovieModel> getCollectionFor(String username)
    {
        byte[] h = hashOf(username);
        Long userId = userRepo.findUserIdByUsernameHash(h)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        Long albumId = ensurePrimaryAlbumId(h, userId);

        var rows = movieListDao.findMoviesForUsername(username, albumId);

        var result = new ArrayList<MovieModel>(rows.size());
        for (var r : rows) {
            result.add(new MovieModel(r.getId(), r.getMovieName(), r.getDirector(), r.getRating(), r.getVideoType()));
        }
        return result;
    }

    /**
     * Returns the movies for the specified album belonging to the current user
     */
    @Override
    public List<MovieModel> getCollectionFor(String username, Long albumId)
    {
        var rows = movieListDao.findMoviesForUsername(username, albumId);
        var result = new ArrayList<MovieModel>(rows.size());
        for (var r : rows) {
            result.add(new MovieModel(r.getId(), r.getMovieName(), r.getDirector(), r.getRating(), r.getVideoType()));
        }
        return result;
    }

    /**
     * Creates a new movie with resolved director/rating/video type and links it into the user's primary album
     */
    @Override
    @Transactional
    public void addMovieFor(String username, MovieModel movie)
    {
        byte[] h = hashOf(username);
        Long userId = userRepo.findUserIdByUsernameHash(h).orElseThrow();
        Long albumId = ensurePrimaryAlbumId(h, userId);

        boolean dup = albumMovieRepo.existsMovieNameInAlbum(h, albumId, movie.getMovieName());
        if (dup) throw new IllegalArgumentException("This movie is already in your collection.");

        final String full = movie.getDirector() != null ? movie.getDirector().trim() : "";
        final int idx = full.lastIndexOf(' ');
        final String firstName = (idx > 0) ? full.substring(0, idx).trim() : full;
        final String lastName = (idx > 0) ? full.substring(idx + 1).trim() : "";

        DirectorEntity director = directorRepo
                .findByDirectorFirstNameAndDirectorLastName(firstName, lastName)
                .orElseGet(() -> {
                    DirectorEntity d = new DirectorEntity();
                    d.setDirectorFirstName(firstName);
                    d.setDirectorLastName(lastName);
                    return directorRepo.save(d);
                });

        String ratingStr = movie.getRating() != null ? movie.getRating().trim() : "";
        RatingEntity rating = ratingRepo.findByRating(ratingStr)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating: " + ratingStr));

        String uiType = movie.getVideoType() != null ? movie.getVideoType().trim() : "";
        String canonicalType = normalizeVideoType(uiType);
        VideoTypeEntity vt = videoTypeRepo.findByType(canonicalType)
                .orElseThrow(() -> new IllegalArgumentException("Invalid video type: " + uiType));

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

    /**
     * Normalizes a UI string like "Blu-Ray" to a canonical video type such as Blu-Ray
     * @param uiType
     * @return
     */
    private static String normalizeVideoType(String uiType)
    {
        if (uiType == null) return "";
        String t = uiType.trim();
        if (t.equalsIgnoreCase("Blu-Ray")) return "Blu-ray";
        if (t.equalsIgnoreCase("DVD")) return "DVD";
        if (t.equalsIgnoreCase("VHS")) return "VHS";
        return t;
    }


    /**
     * Updates an owned movie's core fields (name, director, rating, video type)
     *  after authorization and lookups
     */
    @Override
    public void updateMovieFor(String username, Long movieId, MovieModel updated)
    {
        byte[] h = hashOf(username);
        if (!albumMovieRepo.userOwnsMovie(h, movieId))
            throw new IllegalStateException("Not authorized to edit this movie.");

        final String full = updated.getDirector() != null ? updated.getDirector().trim() : "";
        final int idx = full.lastIndexOf(' ');
        final String firstName = (idx > 0) ? full.substring(0, idx).trim() : full;
        final String lastName = (idx > 0) ? full.substring(idx + 1).trim() : "";

        var director = directorRepo
                .findByDirectorFirstNameAndDirectorLastName(firstName, lastName)
                .orElseGet(() -> {
                    var d = new com.gcu.data.entity.DirectorEntity();
                    d.setDirectorFirstName(firstName);
                    d.setDirectorLastName(lastName);
                    return directorRepo.save(d);
                });

        var rating = ratingRepo.findByRating(updated.getRating())
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating: " + updated.getRating()));

        String canonicalType = normalizeVideoType(updated.getVideoType());
        var vt = videoTypeRepo.findByType(canonicalType)
                .orElseThrow(() -> new IllegalArgumentException("Invalid video type: " + updated.getVideoType()));

        var movieOpt = movieRepo.findById(movieId);
        if (movieOpt.isEmpty()) throw new IllegalStateException("Movie not found.");

        var m = movieOpt.get();
        m.setMovieName(updated.getMovieName());
        m.setDirectorId(director.getId());
        m.setRatingId(rating.getId());
        m.setVideoTypeId(vt.getId());
        movieRepo.save(m);
    }

    /**
     * Removes a movie link from the specified album and deletes
     *  the movie entity if it is no longer linked to any album
     */
    @Override
    public void deleteMovieFor(String username, Long albumId, Long movieId)
    {
        byte[] h = hashOf(username);
        if (!albumMovieRepo.userOwnsMovie(h, movieId))
            throw new IllegalStateException("Not authorized to delete this movie.");

        Long userId = userRepo.findUserIdByUsernameHash(h)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        Long primary = ensurePrimaryAlbumId(h, userId);

        if (!primary.equals(albumId)) {
            albumMovieRepo.deleteFromUserAlbum(h, albumId, movieId);
            return;
        }

        albumMovieRepo.deleteFromUserAlbum(h, albumId, movieId);
        int refs = albumMovieRepo.countAlbumLinksForMovie(movieId);
        if (refs == 0) movieRepo.deleteById(movieId);
    }

    private Long ensurePrimaryAlbumId(byte[] usernameHash, Long userId) 
    {
        return albumRepo.findPrimaryAlbumIdByUsernameHash(usernameHash)
                .orElseGet(() -> {
                    var a = new AlbumEntity();
                    a.setUserId(userId);
                    a.setName("My Collection");
                    a.setDescription(null);
                    a.setUpdatedTime(java.time.LocalDateTime.now());
                    return albumRepo.save(a).getId();
                });
    }

    /**
     * Lists all albums for the current user as lightweight AlbumModel DTOs
     */
    @Override
    public List<AlbumModel> getAlbumsFor(String username)
    {
        var list = albumRepo.findAllForUsernameHash(hashOf(username));
        var out = new ArrayList<AlbumModel>(list.size());
        for (var a : list) 
        	out.add(new AlbumModel(a.getId(), a.getName(), a.getDescription()));
        return out;
    }

    /**
     * Creates a new album owned by the current user with
     *  the given name and optional description
     */
    @Override
    public Long createAlbumFor(String username, String name, String description)
    {
        byte[] h = hashOf(username);
        Long userId = userRepo.findUserIdByUsernameHash(h)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        var a = new AlbumEntity();
        a.setUserId(userId);
        a.setName(name);
        a.setDescription(description);
        a.setUpdatedTime(java.time.LocalDateTime.now());
        return albumRepo.save(a).getId();
    }

    /**
     * Adds an existing movie to the given album without duplicating additional metadata
     */
    @Override
    public void addMovieToAlbum(String username, Long albumId, Long movieId)
    {
        albumMovieRepo.addMovieToAlbum(albumId, movieId);
    }

    /**
     * Returns the id of the user’s primary album, creating it on demand if it does not yet exist
     */
    @Override
    public Long getOrCreatePrimaryAlbumId(String username)
    {
        byte[] h = hashOf(username);
        Long userId = userRepo.findUserIdByUsernameHash(h)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        return ensurePrimaryAlbumId(h, userId);
    }
    
    /**
     * deletes the specified user-owned album after
     *  removing its links, disallowing deletion of the primary “My Collection” album.
     */
	@Override
	public void deleteAlbumFor(String username, Long albumId)
	{
	    if (albumId == null)
	        throw new IllegalArgumentException("Album not specified.");
	
	    byte[] h = hashOf(username);
	
	    Long primaryId = getOrCreatePrimaryAlbumId(username);
	    if (primaryId.equals(albumId))
	        throw new IllegalArgumentException("You cannot delete the 'My Collection' album.");

	    boolean owned = getAlbumsFor(username).stream()
	            .anyMatch(a -> a.getId().equals(albumId));
	    if (!owned)
	        throw new IllegalArgumentException("Album not found or not yours.");

	    albumMovieRepo.deleteAllFromUserAlbum(h, albumId);
	    albumRepo.deleteById(albumId);
	}

	/**
	 * Returns the user’s wishlist as rows containing movie identity and display fields
	 */
	@Override
	public java.util.List<WishlistRow> getWishlistFor(String username) 
	{
		return movieListDao.findWishlistForUser(hashOf(username));
	}
	
	/**
	 * Adds a movie to the user’s wishlist if it is not already present
	 */
	@Override
	@Transactional
	public void addToWishlist(String username, Long movieId) 
	{
	    byte[] h = hashOf(username);
	    if (wishlistRepo.existsInWishlist(h, movieId)) 
	    	return;
	    Long userId = userRepo.findUserIdByUsernameHash(h)
	            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
	    wishlistRepo.insert(userId, movieId);
	}
	
	/**
	 * Moves a wishlist movie into the user’s primary collection,
	 *  optionally updating its video format, then removes it from the wishlist.
	 */
	@Override
	@Transactional
	public void moveWishlistItemToCollection(String username, Long movieId,
	                                         String format) 
	{
	    byte[] h = hashOf(username);
	
	    Long userId = userRepo.findUserIdByUsernameHash(h)
	            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
	    Long albumId = getOrCreatePrimaryAlbumId(username);
	
	    if (format != null && !format.isBlank()) 
	    {
	        String canonicalType = normalizeVideoType(format);
	        var vt = videoTypeRepo.findByType(canonicalType)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid format: " + format));
	        var mOpt = movieRepo.findById(movieId);
	        if (mOpt.isPresent()) 
	        {
	            var m = mOpt.get();
	            m.setVideoTypeId(vt.getId());
	            movieRepo.save(m);
	        }
	    }
	
	    var movieName = movieRepo.findById(movieId)
	            .map(com.gcu.data.entity.MovieEntity::getMovieName)
	            .orElseThrow(() -> new IllegalStateException("Movie not found: " + movieId));
	    if (!albumMovieRepo.existsMovieNameInAlbum(h, albumId, movieName)) 
	        albumMovieRepo.addMovieToAlbum(albumId, movieId);
	
	    wishlistRepo.deleteFromWishlist(h, movieId);
	}
	
	/**
	 * Removes a movie entry from the user’s wishlist
	 */
	@Override
	@Transactional
	public void removeFromWishlist(String username, Long movieId) 
	{
	    wishlistRepo.deleteFromWishlist(hashOf(username), movieId);
	}

	/**
	 * Creates a new movie and adds it to the current user's wishlist.
	 */
	@Override
	@Transactional
	public void createMovieAndAddToWishlist(String username, MovieModel movie) {
	
	    byte[] h = hashOf(username);
	    Long userId = userRepo.findUserIdByUsernameHash(h)
	            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
	
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
	
	    String ratingStr = movie.getRating() != null ? movie.getRating().trim() : "";
	    RatingEntity rating = ratingRepo.findByRating(ratingStr)
	        .orElseThrow(() -> new IllegalArgumentException("Invalid rating: " + ratingStr));
	
	    String uiType = (movie.getVideoType() == null || movie.getVideoType().isBlank())
	                    ? "DVD" : movie.getVideoType().trim();
	    String canonicalType = normalizeVideoType(uiType);
	    VideoTypeEntity vt = videoTypeRepo.findByType(canonicalType)
	        .orElseThrow(() -> new IllegalArgumentException("Invalid video type: " + uiType));
	
	    MovieEntity m = new MovieEntity();
	    m.setMovieName(movie.getMovieName());
	    m.setDirectorId(director.getId());
	    m.setRatingId(rating.getId());
	    m.setVideoTypeId(vt.getId());
	    var saved = movieRepo.save(m);
	
	    if (!wishlistRepo.existsInWishlist(h, saved.getId())) {
	        wishlistRepo.insert(userId, saved.getId());
	    } else {
	        throw new IllegalArgumentException("This movie is already in your wishlist.");
	    }
	}
	

	@Override
	public void init() { System.out.println("init"); }
	
	@Override
	public void destroy() { System.out.println("destroy"); }
}