package com.gcu.data.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.gcu.data.entity.RatingEntity;

public interface RatingRepository extends CrudRepository<RatingEntity, Long>
{
	Optional<RatingEntity> findByRating(String rating);
}
