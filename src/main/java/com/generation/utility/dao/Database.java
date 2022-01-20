package com.generation.utility.dao;
import java.sql.*;
import java.util.*;

//La classe Database all'interno di Utility � un esempio del pattern FACADE.
//FACADE � un pattern STRUTTURALE ossia funge da linea guida per altre classi.
//La FACADE ha il compito di NASCONDERE COME UNA FACCIATA ci� che succede all'interno della classe
//ATTENZIONE: non nasconde per sicurezza ma per ordine.
//Mentre ad esempio l'interfaccia, gli aggregatori etc. avevano un motivo di sicurezza
//la Facade non vuole incrementare la sicurezza ma semplicemente migliorare il lavoro del
//programmatore.
//Solo la classe Database conoscer� le modalit� con cui il programma parla al DB:
//Connection, PreparedStatement, ResultSet, etc.
//Tutto ci� che riguarda queste classi viene nascosto da Database che appunto diventa
//l'esempio del pattern FACADE.
//La logica della FACADE � nascondere il procedimento per mostrare il risultato.
//Chi interagisce con la FACADE in questo caso? DAO.
//In questo modo facciamo s� che una classe si occupi da sola di tutto il funzionamento
//degli elementi che la compongono, un po' come abbiamo fatto finora con ArrayList, List, etc.
public class Database
{
	private Connection c;
	
	public Database(String nomeDb, String user, String password)
	{
		String percorso = "jdbc:mysql://esilxl0nthgloe1y.chr7pe7iynqr.eu-west-1.rds.amazonaws.com:3306/" + nomeDb + "?";
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.c = DriverManager.getConnection(percorso, user, password);
		}
		catch(Exception e)
		{
			System.out.println("Catch del costruttore della classe Database\nControlla user, password e nomeDb");
			e.printStackTrace();
		}
	}//Fine del costruttore
	
	//I metodi row e rows mi servono per read() mentre update() per create/update/delete
	public List<Map<String, String>> rows(String query, String... params)
	{
		List<Map<String, String>> ris = new ArrayList<Map<String, String>>();
		
		try
		{
			PreparedStatement ps = c.prepareStatement(query);
			
			for(int i = 0; i < params.length; i++)
				ps.setString(i + 1, params[i]);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Map<String, String> riga = new LinkedHashMap<String, String>();
				
				for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
					riga.put(rs.getMetaData().getColumnLabel(i).toLowerCase(), rs.getString(i));

				ris.add(riga);
			}
		}//Fine di try
		catch(Exception e)
		{
			System.out.println("Problema nel metodo rows di Database");
			e.printStackTrace();
		}
		return ris;
	}//Fine di rows
	
	public Map<String, String> row(String query, String...params)
	{
		Map<String, String> ris = new LinkedHashMap<String, String>();
		try
		{
			ris = rows(query, params).get(0);
		}
		catch(Exception e)
		{
			ris = null;
		}
		return ris;
	}//Fine di row
	
	public boolean update(String query, String... params)
	{
		boolean ris;
		try
		{
			PreparedStatement ps = c.prepareStatement(query);
			for(int i = 0; i < params.length; i++)
				ps.setString(i + 1, params[i]);
			ps.executeUpdate();
			ris = true;
		}
		catch(Exception e)
		{
			System.out.println("Catch nel metodo update di Database in Utility");
			ris = false;
		}
		return ris;
	}//Fine di update
}