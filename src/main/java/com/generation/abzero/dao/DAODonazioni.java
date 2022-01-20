package com.generation.abzero.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.generation.abzero.entities.Donazione;
import com.generation.utility.dao.Database;

public class DAODonazioni 
{
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	Database db;
	
	//Metodo read()
	public List<Donazione> read(String query, String... params)
	{
		List<Donazione> ris = new ArrayList<Donazione>();
		List<Map<String, String>> righe = db.rows(query, params);
		for(Map<String, String> riga : righe)
		{
			Donazione d = context.getBean(Donazione.class, riga);
			ris.add(d);
		}
		return ris;
	}
	
	//Metodo per leggere tutte le Donazioni nel DB
	public List<Donazione> leggiTutti()
	{
		return read("select * from donazione");
	}
	
	//Metodo per prendere dal DB l'oggetto Donazione con l'ID passato
	public Donazione leggiPerId(int id)
	{
		return read("select * from donazione where id = ?", id + "").get(0);
	}
	
	//Metodo per leggere dal DB tutte le donazioni fatte dal donatore specificato nel parametro
	public List<Donazione> leggiPerDonatore(int idDon)
	{
		return read("select * from donazione where iddon = ?", idDon + "");
	}
	
	//Metodo per leggere dal DB tutte le donazioni fatte nella sede specificata nel parametro
	public List<Donazione> leggiPerSede(int idSede)
	{
		return read("select * from donazione where idsede = ?", idSede + "");
	}
	
	//Metodo per filtrare una lista di donazioni (passata come parametro)
	//Per un tipo di donazione specifico (passato tramite idTipo)
	//Restituendo una nuova lista contente esclusivamente le donazioni del tipo specificato
	public List<Donazione> filtraTipi(List<Donazione> listaPiena, int idTipo)
	{
		List<Donazione> listaFiltrata = new ArrayList<Donazione>();
		for(Donazione d : listaPiena)
			if(d.getIdTipo() == idTipo)
				listaFiltrata.add(d);
		return listaFiltrata;	
	}
	
	//Metodo per filtrare una lista di donazioni per data
	public List<Donazione> leggiPerData(String data)
	{
		//Date d = Date.valueOf(data);  
		//String dataFormattata = d.toString();
		//System.out.println("DATA SECONDO DATE: " + d.toString());
		return read("select * from donazione where datadon = \"" + data + "\"");  
	}
	
	public List<Donazione> leggiPerMese(int mese, int anno){
		mese++;
		String dataMin = anno + "-" + mese + "-1";
		if(mese == 12) {
			mese = 1;
			anno++;
		}else {
			mese++;
		}
		String dataMax = anno + "-" + mese + "-1";
		return read("select * from donazione where datadon >= ? and datadon < ?", dataMin, dataMax);
	}
	
	public List<Donazione> leggiDonazioniPassatePerDonatore(int idDon){
		return read("select * from donazione where datadon < NOW() and iddon = ?" , idDon + "");
	}
	
	//Metodo per l'inserimento di una Donazione nel DB
	public boolean creaDonazione(Donazione d)
	{
		String query = "insert into donazione (datadon,note,iddon,idsede,idtipo) values (?,?,?,?,?)";
		System.out.println(d);
		//Richiamiamo il metodo update al quale passiamo la query e tutti i parametri necessari
		return db.update(query, d.getDataDon() + "", d.getNote(), d.getIdDon() + "", d.getIdSede() + "", d.getIdTipo() + "");
	}
	
	//Metodo per la modifica dei dati di una donazione 
	public boolean aggiornaDonazione(Donazione d)
	{
		String query = "update donazione set datadon = ?, note = ?, iddon = ?, idsede = ?, idtipo = ? where id = ?";
		return db.update(query, d.getDataDon() + "", d.getNote(), d.getIdDon() + "", d.getIdSede() + "", d.getIdTipo() + "", d.getId() + "");
	}
	
	//Metodo per l'eliminazione di una Donazione nel DB tramite id
	public boolean eliminaDonazione(int idDon)
	{
		String query = "delete from donazione where id = ?";
		return db.update(query, idDon + "");
	}
	
	public List<Donazione> leggiUltimaDonazione(int idDon)
	{
		String query = "SELECT datadon FROM donazione AS d INNER JOIN (SELECT MAX(datadon) AS ultimadata FROM donazione GROUP BY iddon) md ON d.datadon = md.ultimadata WHERE d.iddon = ?";
		return read(query, idDon + "");
	}
}
