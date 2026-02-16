package com.gcu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcu.business.CollectionsBusinessInterface;
import com.gcu.business.CollectionsBusinessService;
import com.gcu.data.dao.MovieListDao;
import com.gcu.data.repository.CollectionRepository;
import com.gcu.data.repository.DirectorRepository;
import com.gcu.data.repository.MovieRepository;
import com.gcu.data.repository.RatingRepository;
import com.gcu.data.repository.UserRepository;
import com.gcu.data.repository.VideoTypeRepository;

@Configuration
public class SpringConfig 
{
	@Bean(initMethod="init", destroyMethod="destroy")
	public CollectionsBusinessInterface collectionsBusinessService(
            CollectionRepository collectionRepo,
            DirectorRepository directorRepo,
            RatingRepository ratingRepo,
            VideoTypeRepository videoTypeRepo,
            MovieRepository movieRepo,
            UserRepository userRepo, MovieListDao movieListDao) 
	{
        return new CollectionsBusinessService(
                collectionRepo,
                directorRepo,
                ratingRepo,
                videoTypeRepo,
                movieRepo,
                userRepo,
                movieListDao
        );
    }
}
