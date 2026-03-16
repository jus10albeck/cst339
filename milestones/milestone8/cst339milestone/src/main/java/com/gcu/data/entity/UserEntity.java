package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user")
public class UserEntity
{
	@Id
	private Long id;
	
	@Column("first_name")
	private String firstName;
	
	@Column("last_name")
	private String lastName;
	
	@Column("login_id")
	private Long loginId;
	
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
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * getters
	 * @return
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * getters
	 * @return
	 */
	public Long getLoginId()
	{
		return loginId;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setLoginId(Long loginId)
	{
		this.loginId = loginId;
	}
}
