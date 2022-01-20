package com.generation.abzero.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.generation.abzero.entities.Tipo;
import com.generation.utility.dao.Database;

public class DAOTipi {
	
	@Autowired
	private Database db;

	@Autowired
	private ApplicationContext context;
	//Il metodo read restituisce un ArrayList contenente i tipi di donazione
	public List<Tipo> read(String query, String... params)
	{
		List<Tipo> ris = new ArrayList<Tipo>();
		List<Map<String,String>> righe = db.rows(query, params);
		
		for(Map<String,String> riga : righe)
		{
			Tipo t = context.getBean(Tipo.class, riga);
			ris.add(t);
		}
		return ris;
	}
	//Creiamo un metodo per farci restituire dal DB la lista dei tipi di donazioni che utilizza read() al quale passa la query
	//select * from tipo.
	public List<Tipo> leggiTutti()
	{
		return read("select * from tipo");
	}
	//Creiamo un metodo per farci restituire dal DB il tipo in base all'id 
	Tipo leggiPerId(int id)
	{
		return read("select * from tipo where id = ?", id+"").get(0);
	}
	

}