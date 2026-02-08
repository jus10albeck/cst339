package com.gcu.model;

public class OrderModel
{
	private Long id;
	private String orderNo;
	private String productName;
	private float price;
	private int quantity;
	
	public OrderModel(Long id, String orderNo,
			String productName, float price, int quantity)
	{
		this.id = id;
		this.orderNo = orderNo;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
	}
	
	// --- id getter/setter ---
	public Long getId() 
	{
		return id;
	}
	public void setId(Long id) 
	{
		this.id = id;
	}
	
	// --- orderNo getter/setter ---
	public String getOrderNo() 
	{
		return orderNo;
	}
	public void setOrderNo(String orderNo) 
	{
		this.orderNo = orderNo;
	}
	
	// --- productName getter/setter ---
	public String getProductName() 
	{
		return productName;
	}
	public void setProductName(String productName) 
	{
		this.productName = productName;
	}
	
	// --- price getter/setter ---
	public float getPrice() 
	{
		return price;
	}
	public void setPrice(float price) 
	{
		this.price = price;
	}
	
	// --- quantity getter/setter ---
	public int getQuantity() 
	{
		return quantity;
	}
	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}
	
}