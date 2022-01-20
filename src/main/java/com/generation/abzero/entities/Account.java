package com.generation.abzero.entities;

import com.generation.utility.entities.Entity;

public abstract class Account extends Entity {
	
	// proprietà dell'account
	private String email; // Primary Key
	private String password;
	private boolean staff;
	private String sessionID;

	// metodi get e set delle proprietà
	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public boolean isStaff() {
		return staff;
	}

	public void setStaff(boolean staff) 
	{
		this.staff = staff;
	}
	
	public String getSessionID()
	{
		return sessionID;
	}
	
	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}
}