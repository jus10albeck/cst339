package com.gcu.business;

import java.util.ArrayList;
import java.util.List;

import com.gcu.model.MovieModel;

public class CollectionsBusinessService implements CollectionsBusinessInterface
{
	@Override
	public void test() 
	{
		System.out.println("Test message from CollectionsBusinessService");
	}
	
	private List<MovieModel> defaultList()
	{
		List<MovieModel> movies = new ArrayList<MovieModel>();
		movies.add(new MovieModel(0L, "Jurassic Park", "Stephen Spielberg", "PG-13", "DVD"));
		movies.add(new MovieModel(1L, "Alien", "Ridley Scott", "R", "DVD"));
		movies.add(new MovieModel(2L, "Aliens", "James Cameron", "R", "DVD"));
		movies.add(new MovieModel(3L, "The Prestige", "Christopher Nolan", "R", "DVD"));
		return movies;
	}
	
	@Override
	public List<MovieModel> getCollectionFor(String username) 
	{
		if ("justinA".equalsIgnoreCase(username))
			return defaultList();
		
		return new ArrayList<>();
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
}
