package com.gcu.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.gcu.data.entity.MovieEntity;

public interface MovieRepository extends CrudRepository<MovieEntity, Long>
{}
