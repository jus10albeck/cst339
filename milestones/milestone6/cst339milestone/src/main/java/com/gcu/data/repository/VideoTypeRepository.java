package com.gcu.data.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.gcu.data.entity.VideoTypeEntity;

public interface VideoTypeRepository extends CrudRepository<VideoTypeEntity, Long>
{
	Optional<VideoTypeEntity> findByType(String type);
}
