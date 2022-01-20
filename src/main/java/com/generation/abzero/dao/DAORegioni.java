package com.generation.abzero.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.generation.abzero.entities.Regione;
import com.generation.utility.dao.Database;

public class DAORegioni {
	
	@Autowired
	private Database db;
	@Autowired
	private ApplicationContext context;
	

	//Metodo che serve per leggere i record, può ricevere dei parametri
	public List<Regione> read(String query, String... params)
	{
		List<Regione> ris = new ArrayList<Regione>();
		List<Map<String,String>> righe = db.rows(query, params);
		
		for(Map<String,String> riga : righe)
		{
			Regione r = (Regione) context.getBean(Regione.class, riga);
			ris.add(r);
		}
		return ris;	
	}
	
	
	//metodo per leggere tutte le regioni
	public List<Regione> leggiTutti()
	{
		return read("select * from regione");
	}
	
	
	//metodo che ritorna una regione cercata per id
	public Regione leggiPerId(int id)
	{
		try
		{
			//abbiamo il .get(0) perchè abbiamo una List, quindi avendo solo un record
			//dobbiamo riprendere la prima posizione.
			return read("select * from regione where id = ?", id+"").get(0);
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
}
