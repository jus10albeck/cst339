package com.gcu.api.dto;

public record ProductDto(
		Long id,
		String movieName,
		String director,
		String rating,
		String videoType
){}