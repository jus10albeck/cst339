package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("wishlist")
public class WishlistEntity {
    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("movie_id")
    private Long movieId;

    /**
     * getters
     * @return
     */
    public Long getId() 
    { 
    	return id; 
    }
    
    /**
     * setters
     * @param id
     */
    public void setId(Long id) 
    { 
    	this.id = id; 
    }

    /**
     * getters
     * @return
     */
    public Long getUserId() 
    { 
    	return userId; 
    }

    /**
     * setters
     * @param id
     */
    public void setUserId(Long userId) 
    { 
    	this.userId = userId; 
    }

    /**
     * getters
     * @return
     */
    public Long getMovieId() 
    {
    	return movieId; 
    }
    
    /**
     * setters
     * @param id
     */
    public void setMovieId(Long movieId) 
    { 
    	this.movieId = movieId; 
    }
}