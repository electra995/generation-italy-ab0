package com.generation.abzero.entities;

import java.sql.Date;

import com.generation.utility.entities.Entity;

public class Donazione extends Entity
{
	//proprietà della donazione
	
	private Date dataDon;
	private String note;
	private int idDon;
	private int idSede;
	private int idTipo;
	
	//metodi get e set delle proprietà
	
	
	public Date getDataDon() 
	{
		return dataDon;
	}
	public void setDataDon(Date dataDon) 
	{
		this.dataDon = dataDon;
	}
	public String getNote() 
	{
		return note;
	}
	public void setNote(String note) 
	{
		this.note = note;
	}
	public int getIdDon() 
	{
		return idDon;
	}
	public void setIdDon(int idDon) 
	{
		this.idDon = idDon;
	}
	public int getIdSede() 
	{
		return idSede;
	}
	public void setIdSede(int idSede) 
	{
		this.idSede = idSede;
	}
	public int getIdTipo() 
	{
		return idTipo;
	}
	public void setIdTipo(int idTipo) 
	{
		this.idTipo = idTipo;
	}
	
	
	
}