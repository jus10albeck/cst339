package com.gcu.business;

import java.util.List;

import com.gcu.model.AlbumModel;
import com.gcu.model.MovieModel;

public interface CollectionsBusinessInterface 
{
	public void test();
	public void init();
	public void destroy();
	
	List<MovieModel> getCollectionFor(String username);
	List<MovieModel> getCollectionFor(String username, Long albumId);
	void addMovieFor(String username, MovieModel movie);
	void updateMovieFor(String username, Long movieId, MovieModel updated);
	void deleteMovieFor(String username, Long albumId, Long movieId);
	List<AlbumModel> getAlbumsFor(String username);
	Long createAlbumFor(String username, String name, String description);
	void addMovieToAlbum(String username, Long albumId, Long movieId);
}
