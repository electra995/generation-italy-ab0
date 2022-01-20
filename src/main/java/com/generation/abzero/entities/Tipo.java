package com.generation.abzero.entities;

import com.generation.utility.entities.Entity;

public class Tipo extends Entity
{
	private String nome;
	private int giorniAttesaM;
	private int giorniAttesaF;
	
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public int getGiorniAttesam()
	{
		return giorniAttesaM;
	}
	public void setGiorniAttesaM(int giorniAttesaM)
	{
		this.giorniAttesaM = giorniAttesaM;
	}
	public int getGiorniAttesaF()
	{
		return giorniAttesaF;
	}
	public void setGiorniAttesaF(int giorniAttesaF)
	{
		this.giorniAttesaF = giorniAttesaF;
	}
}
