package com.generation.abzero.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.generation.abzero.entities.Staff;
import com.generation.utility.dao.Database;

public class DAOStaff {
	
	@Autowired
	private Database db;
	@Autowired
	private ApplicationContext context;
	
	//Metodo che serve per fare qualsiasi tipo di Read al db. Restituisce l'ArrayList contenente i membri dello Staff
	public List<Staff> read(String query, String... params)
	{
		List<Staff> ris = new ArrayList<Staff>();
		List<Map<String,String>> righe = db.rows(query, params);
		
		for(Map<String,String> riga : righe)
		{
			Staff s = (Staff) context.getBean(Staff.class, riga);
			ris.add(s);
		}
		return ris;	
	}
	
	//Metodo che legge tutti i valori di staff, ritorna tutti i record della tabella staff
	public List<Staff> leggiTutti()
	{
		return read("select * from staff");
	}
	
	
	//Metodo che cerca per email. Restituisce tutti i membri dello staff con la e-mail specificata
	public Staff leggiPerEmail(String email)
	{
		try
		{
			//0 perchè avendo richiamato il metodo read che rende una lista, dobbiamo prendere solo un valore
			//che sarà il primo
			return read("select * from staff where email = ?", email +"").get(0);
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
}
