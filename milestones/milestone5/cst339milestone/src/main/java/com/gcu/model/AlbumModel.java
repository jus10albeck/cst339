package com.gcu.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlbumModel
{
	 private Long id;
	 
	 @NotBlank(message = "Album name is required")
	 @Size(max = 80, message = "Album name must be less than 80 characters")
	 private String name;
	 
	 @Size(max = 255, message = "Description must be less than 255 characters")
	 private String description;
	 
	 public AlbumModel() {}
	 
	 public AlbumModel(Long id, String name, String description)
	 {
		 this.setId(id);
		 this.setName(name);
		 this.setDescription(description);
	 }

	 public Long getId()
	 {
		return id;
	 }

	 public void setId(Long id)
	 {
		this.id = id;
	 }

	 public String getName()
	 {
		return name;
	 }

	 public void setName(String name)
	 {
		this.name = name;
	 }

	 public String getDescription()
	 {
		return description;
	 }

	 public void setDescription(String description)
	 {
		this.description = description;
	 }
}
