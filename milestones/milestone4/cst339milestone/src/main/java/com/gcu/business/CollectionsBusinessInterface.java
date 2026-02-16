package com.gcu.business;

import java.util.List;

import com.gcu.model.MovieModel;

public interface CollectionsBusinessInterface 
{
	public void test();
	public void init();
	public void destroy();
	
	List<MovieModel> getCollectionFor(String username);
	void addMovieFor(String username, MovieModel movie);
	void updateMovieFor(String username, Long movieId, MovieModel updated);
	void deleteMovieFor(String username, Long movieId);
}
