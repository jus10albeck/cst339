package com.gcu.api.dto;

/**
 * A data transfer object representing a movie
 */
public record ProductDto(
		Long id,
		String movieName,
		String director,
		String rating,
		String videoType
){}