package com.gcu.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="collections")
public class CollectionModel 
{
	private List<MovieModel> collection = new ArrayList<MovieModel>();
	
	@XmlElement(name="collection")
	public List<MovieModel> getCollections()
	{
		return this.collection;
	}
	
	public void setCollections(List<MovieModel> collection)
	{
		this.collection = collection;
	}
}
