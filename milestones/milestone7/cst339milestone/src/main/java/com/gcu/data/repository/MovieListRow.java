package com.gcu.data.repository;

public class MovieListRow
{
	private Long id;
    private String movieName;
    private String director;
    private String rating;
    private String videoType;
    
    public MovieListRow(Long id, String movieName,
    		String director, String rating, String videoType) 
    {
        this.id = id;
        this.movieName = movieName;
        this.director = director;
        this.rating = rating;
        this.videoType = videoType;
      }

    
	public Long getId()
	{
		return id;
	}

	public String getDirector()
	{
		return director;
	}

	public String getMovieName()
	{
		return movieName;
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