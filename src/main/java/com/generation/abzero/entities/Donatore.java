package com.generation.abzero.entities;

import java.sql.Date;

import com.generation.utility.entities.Entity;

public class Donatore extends Account
{
	//proprietà del donatore 
	
	private String nome;
	private String cognome; 
	private boolean sesso;
	private Date dob;
	private String cell;
	private String tipoSangue;
	private boolean rh;
	private String regione;
	
	
	//metodi get e set delle proprietà 
	
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public String getCognome() 
	{
		return cognome;
	}
	public void setCognome(String cognome) 
	{
		this.cognome = cognome;
	}
	public boolean isSesso() 
	{
		return sesso;
	}
	public void setSesso(boolean sesso) 
	{
		this.sesso = sesso;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) 
	{
		this.dob = dob;
	}
	public String getCell() 
	{
		return cell;
	}
	public void setCell(String cell) 
	{
		this.cell = cell;
	}
	public String getTipoSangue() 
	{
		return tipoSangue;
	}
	public void setTipoSangue(String tipoSangue) 
	{
		this.tipoSangue = tipoSangue;
	}
	public boolean isRh() 
	{
		return rh;
	}
	public void setRh(boolean rh) 
	{
		this.rh = rh;
	}
	public String getRegione() 
	{
		return regione;
	}
	public void setRegione(String regione) 
	{
		this.regione = regione;
	} 
	
	
}