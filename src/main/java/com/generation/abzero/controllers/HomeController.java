package com.generation.abzero.controllers;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.generation.abzero.dao.DAOAccount;
import com.generation.abzero.entities.Account;
//Controller è un'annotation che serve per comunicare a spring che questa classe contiene mapping

@Controller
public class HomeController
{
	// Autowired è un'annotatio che serve per istanziare automaticamente l'oggetto che è già stato dichiarato
	//nella classe context tramite i Bean. Autowired cerca l'annotation Bean delll'oggetto che
	//ci interessa istanziare in questa classe. (da servirà per richiamare un metodo della sua classe)
	@Autowired
	DAOAccount da;


	//La richiesta tramite il suo indirizzo di base (indirizzo di rete nel nostro server
	//localhost:8080 in questo caso, perché stiamo usando il server
	//che sta all'interno del nostro computer) a cui aggiungiamo lo / , verrà gestito da spring dentro il
	//metodo Home che ha il mapping /.
	@GetMapping("/")
	public String home(HttpSession session)
	{
		//Controllimao se l'utente è logato o no, tramite il valore della sessionID
		if(session.getAttribute("sessionID") != null)
		{
			//Se esiste, il metodo session.getAttribute restituisce la sessionID e la trasmette al metodo
			//leggiPerSessionID che a sua volta
			//trasmette i dati dell'utente associati al donatore o allo staff con quel sessionID(vedi DAOAccount) all'oggetto Account a.
			Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
			System.out.println(a);
		}
		//inserendo una pagina web nel return diciamo a spring di trasmettere quella pagina web all'utente
		return "index.html";
	}

	@GetMapping("/donazioni")
	public String donazioni()
	{
		return "donazioni.html";
	}

	@GetMapping("/contattaci")
	public String contatti()
	{
		return "contattaci.html";
	}
	
	@GetMapping("/sedi")
	public String sedi()
	{
		return "sedi.html";
	}
	
	@GetMapping("/statistiche")
	public String statistiche()
	{
		return "statistiche.html";
	}

	
}
