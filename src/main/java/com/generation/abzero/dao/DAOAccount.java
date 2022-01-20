package com.generation.abzero.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.generation.abzero.entities.Account;
import com.generation.abzero.entities.Donatore;
import com.generation.utility.dao.Database;

public class DAOAccount 
{
	@Autowired
	Database db;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private DAOStaff ds;
	
	@Autowired
	private DAODonatori dd;

	// Lettura generica con creazione List<Account>
	public List<Account> read(String query, String... params) 
	{
		List<Account> ris = new ArrayList<Account>();
		List<Map<String, String>> righe = db.rows(query, params);

		for (Map<String, String> riga : righe) 
		{
			Account a = (Account) context.getBean("accountMappa", riga);
			ris.add(a);
		}
		return ris;
	}

	// Metodo per ottenere una lista di tutti gli oggetti Account nel db
	List<Account> leggiTutti() 
	{
		return read("SELECT * FROM account");
	}

	// Metodo per restituire un oggetto Account usando email e password presi dai
	// parametri
	public Account leggiPerCredenziali(String email, String password) 
	{
		try 
		{
			return read("SELECT * FROM account WHERE email = ? AND password = ?", email, password).get(0);
		} catch (IndexOutOfBoundsException e) 
		{
			return null;
		}
	}
	
	// Metodo per verificare la presenza di un account con l'email passata
	// Restituisce true se esiste un account con l'email passata, false se non
	// esiste
	public boolean controlloEmail(String email) 
	{
		return read("SELECT * FROM account WHERE email = ?", email).size() > 0;
	}
	
	public List<Donatore> leggiDonatori(){
		List<Account> listaAcc = read("SELECT * FROM account WHERE staff = 0");
		List<Donatore> listaDon = new ArrayList<Donatore>();
		for (Account account : listaAcc) {
			listaDon.add(dd.leggiPerEmail(account.getEmail()));
		}
		return listaDon;
	}

	public Account leggiPerSessionID(String sessionID) 
	{
		try 
		{
			Account a = read("SELECT * FROM account WHERE sessionID = ?", sessionID).get(0);
			Account acc;
			if(a.isStaff()) 
			{
				acc = ds.leggiPerEmail(a.getEmail());
			}else 
				acc = dd.leggiPerEmail(a.getEmail());
			
			acc.setPassword(a.getPassword());
			acc.setStaff(a.isStaff());
			acc.setSessionID(a.getSessionID());
			return acc;
		} catch (IndexOutOfBoundsException e) 
		{
			return null;
		}
	}

	public boolean impostaSessionID(String email, String sessionID) 
	{
		return db.update("UPDATE account SET sessionid = ? WHERE email = ?", sessionID, email);
	}

	public String leggiSessionID(String email) 
	{
		try 
		{
			return read("SELECT * FROM account WHERE email = ?", email).get(0).getSessionID();
		} catch (IndexOutOfBoundsException e) 
		{
			return null;
		}
	}

	public boolean eliminaSessionID(String sessionID) 
	{
		return db.update("UPDATE account SET sessionid = null WHERE sessionid = ?", sessionID);
	}


	// Metodo per la creazione dell'account nel database, usando l'Account passato
	// come parametro
	public boolean creaAccount(Account a) 
	{
		String query = "INSERT INTO account (email, password, staff) VALUES (?,?,?)";
		return db.update(query, a.getEmail(), a.getPassword(), (a.isStaff() ? "1" : "0"));
	}

	// Metodo per l'eliminazione di un account nel database, usando l'email (Primary
	// Key) passato come parametro
	public boolean eliminaAccount(String email) 
	{
		String query = "DELETE FROM account WHERE email = ?";
		return db.update(query, email);
	}

	// Metodo per la modifica della password di un account, prendendo l'email
	// (Primary key)
	// e la nuova password dall'oggetto Account passato come parametro
	public boolean aggiornaPassword(Account a) 
	{
		String query = "UPDATE account SET password = ? WHERE email = ?";
		return db.update(query, a.getPassword(), a.getEmail());
	}

}
