package com.gcu.data.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.gcu.data.entity.DirectorEntity;

public interface DirectorRepository extends CrudRepository<DirectorEntity, Long>
{
	Optional<DirectorEntity> findByDirectorFirstNameAndDirectorLastName
								(String directorFirstname, String directorLastName);
}
