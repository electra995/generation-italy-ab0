package com.generation.abzero.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.generation.abzero.entities.Donatore;
import com.generation.utility.dao.Database;

public class DAODonatori 
{

	// Questa annotation viene utilizzare per dire a Spring di iniettare le
	// proprietà del bean Database all'avvio
	@Autowired
	private Database db;

	// Questo oggetto di classe ApplicationContex serve per accedere ai bean quando
	// siamo all'interno di un metodo
	@Autowired
	private ApplicationContext context;

	/**
	 * Questo metodo che restituisce una lista di oggetti Donatore serve per leggere
	 * dati da una tabella di un database
	 * 
	 * @param query
	 * @param params
	 * @return List<Donatore>
	 */
	public List<Donatore> read(String query, String... params) 
	{
		// Creiamo un arraylist che sarà il nostro return
		List<Donatore> ris = new ArrayList<Donatore>();

		// Creiamo una lista di mappe attraverso il metodo rows presente nella classe
		// Database
		List<Map<String, String>> righe = db.rows(query, params);
		// Trasformiamo la lista di mappe in una lista di oggetti
		for (Map<String, String> riga : righe) 
		{
			// Il metodo getBean ci restituisce un Object per cui bisognerà castarlo a
			// Donatore
			Donatore d = (Donatore) context.getBean("donatoriMappa", riga);
			// Aggiungiamo gli oggetti Donatore alla lista di oggetti Donatore
			ris.add(d);
		} // Fine for
		return ris;
	}// Fine read()

	/**
	 * Questo metodo che restituisce un oggetto Donatore serve per filtrare tramite
	 * email
	 * 
	 * @param email
	 * @return Donatore
	 */
	public Donatore leggiPerEmail(String email) 
	{
		// Poichè il metodo read() restituisce una list di oggetti Donatore mentre il
		// metodo leggiPerEmail() ci restituisce
		// un solo oggetto, dobbiamo dire al metodo di prenedere solo il primo valore
		// all'indice 0
		return read("select * from donatore where email = ?", email).get(0);
	}

	/**
	 * Questo metodo che restituisce una lista di oggetti Donatore serve per
	 * filtrare tramite regione
	 * 
	 * @param idRegione
	 * @return List<Donatore>
	 */
	public List<Donatore> leggiPerRegione(int idRegione) 
	{
		return read("select * from donatore where idregione = ?", idRegione + "");
	}

	/**
	 * Questo metodo che restituisce un boolean serve per creare un oggetto di tipo
	 * Donatore
	 * 
	 * @param d
	 * @return boolean
	 */
	public boolean creaDonatore(Donatore d) 
	{
		String query = "insert into donatore (nome, cognome, sesso, dob, cell, tiposangue, rh, email, regione) values (?,?,?,?,?,?,?,?,?)";
		return db.update(query, d.getNome(), d.getCognome(), (d.isSesso() ? "1" : "0") + "", d.getDob() + "",
				d.getCell(), d.getTipoSangue(), (d.isRh() ? "1" : "0"), d.getEmail(), d.getRegione() + "");
	}

	/**
	 * Questo metodo che restituisce un boolean serve per eliminare un donatore con
	 * un determinato id
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean eliminaDonatore(int id) 
	{
		String query = "delete from donatore where id = ?";
		return db.update(query, id + "");
	}

	/**
	 * Questo metodo che restituisce un boolean serve per modificare un donatore
	 * 
	 * @param d
	 * @return boolean
	 */
	public boolean modificaDonatore(Donatore d) 
	{
		String query = "update donatore set nome = ?, cognome = ?, sesso = ?, dob = ?, cell = ?, tiposangue = ?, rh = ?, email = ?, regione = ? where id = ?";
		return db.update(query, d.getNome(), d.getCognome(), (d.isSesso() ? "1" : "0"), d.getDob() + "", d.getCell(),
				d.getTipoSangue(), (d.isRh() ? "1" : "0"), d.getEmail(), d.getRegione() + "", d.getId() + "");
	}
}
