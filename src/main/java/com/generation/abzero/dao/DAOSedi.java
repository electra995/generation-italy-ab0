package com.generation.abzero.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.generation.abzero.entities.Sede;
import com.generation.utility.dao.Database;

public class DAOSedi {

	@Autowired
	private Database db;


	@Autowired
	private ApplicationContext context;
	//metodo che restituisce un ArrayList contenente le sedi
	public List<Sede> read(String query, String... params)
	{
		List<Sede> ris = new ArrayList<Sede>();
		List<Map<String,String>> righe = db.rows(query, params);

		for(Map<String,String> riga : righe)
		{
			Sede s = context.getBean(Sede.class, riga);
			ris.add(s);
		}
		return ris;
	}

	//Creiamo un metodo per farci restituire dal DB una lista di sedi che utilizza read() al quale passa la query
	public List<Sede> leggiTutti()
	{
		return read("select * from sede");
	}

	//Metodo per farci restituire dal DB una lista di sedi selezionate per regione
	public List<Sede> leggiPerRegione(int idRegione)
	{
		return read("select * from sede where idregione = ?", idRegione+"");
	}
	//Metodo per farci restituire la sede con l'id specificato (Primary Key)
	public Sede leggiPerId(int id)
	{
		//voglio ottenere l'unico elemento della lista
		return read("select * from sede where id =?", id+"").get(0);
	}
}
