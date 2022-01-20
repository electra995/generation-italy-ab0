package com.generation.abzero.entities;
import com.generation.utility.entities.Entity;

public class Sede extends Entity
{
	private String nome;
	private String coordinate;
	private int idRegione;
	
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public String getCoordinate()
	{
		return coordinate;
	}
	public void setCoordinate(String coordinate)
	{
		this.coordinate = coordinate;
	}
	public int getIdRegione()
	{
		return idRegione;
	}
	public void setIdRegione(int idRegione)
	{
		this.idRegione = idRegione;
	}
}
