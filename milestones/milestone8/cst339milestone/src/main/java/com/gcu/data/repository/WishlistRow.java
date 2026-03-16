package com.gcu.data.repository;

public class WishlistRow
{

	private Long id;
    private Long movieId;
    private String movieName;
    private String director;
    private String rating;
    private String videoType;

    public WishlistRow(Long id, Long movieId, String movieName,
                       String director, String rating, String videoType) 
    {
        this.id = id;
        this.movieId = movieId;
        this.movieName = movieName;
        this.director = director;
        this.rating = rating;
        this.videoType = videoType;
    }
    
    public Long getId() 
    { 
    	return id; 
    }
    
    public Long getMovieId() 
    { 
    	return movieId; 
    }
    
    public String getMovieName() 
    { 
    	return movieName; 
    }
    
    public String getDirector() 
    { 
    	return director; 
    }
    
    public String getRating() 
    { 
    	return rating; 
    }
    public String getVideoType() 
    { 
    	return videoType; 	
    }
}