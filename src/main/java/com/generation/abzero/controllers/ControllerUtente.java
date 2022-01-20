package com.generation.abzero.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.generation.abzero.dao.DAOAccount;
import com.generation.abzero.dao.DAODonazioni;
import com.generation.abzero.entities.Account;
import com.generation.abzero.entities.Donatore;
import com.generation.abzero.entities.Donazione;
import com.generation.abzero.entities.Staff;

/* L'annotation controller indica al programma che questa classe è un controller e gestirà i mapping. 
 * L'annotation RequestMapping serve per configurare la mappatura delle richieste  web.
 * Il mapping principale di questa classe è  /areariservata, quindi per accedere a tutti i mapping successivi
 * bisognerà scrivere nell'URL /areariservata/... Questa classe gestirà la parte di sito web accessibile una volta effettuato il login.
 */
@Controller
@RequestMapping("/areariservata")
public class ControllerUtente {
	// L'annotation Autowired serve per istanziare all'interno dell'oggetto
	// specificato tutte le proprietà dell'oggetto stesso. Per richiamare i bean
	// all'interno di un metodo abbiamo bisogno dell'oggetto di classe
	// ApplicationContext non potendo usare un @Autowired.
	@Autowired
	ApplicationContext context;

	@Autowired
	DAOAccount da;

	@Autowired
	DAODonazioni dd;

	// !!!TODO se c'è tempo risolvere il redirect ad index per utente non loggato

	/*
	 * L'annotation GetMapping è una annotation scorciatoria per evitare di
	 * scrivere @RequestMapping(method = RequestMethod.GET) e gestisce le richieste
	 * GET. Creiamo un metodo a cui accediamo quando inseriamo l'URL
	 * /areariservata/. Questo metodo ci restituisce una String e come parametro
	 * viene passata la HTTPSession.
	 */
	@GetMapping("/")
	public String home(HttpSession session) {
		// Impostiamo una variabile di tipo String che sarà il nostro return a /?
		String ris = "../index.html";
		// Se all'interno dell'oggetto session troviamo l'attributo sessionID che
		// corrisponde all'UUID allora entriamo nell if
		if (session.getAttribute("sessionID") != null) {
			/*
			 * Creiamo quindi un oggetto a di tipo Account attraverso l'oggetto da di tipo
			 * DAOAccount filtrando i risultati per SessionID attraverso il metodo
			 * leggiPerSessionID. Questo metodo ritorna uno specifico account presente nel
			 * Database che ha come valore di sessionID (presente come colonna nel database
			 * nella tabella account) quello preso dalla HTTPSession. Poichè il metodo
			 * getAttribute ci restituisce un oggetto di tipo Session mentre il nostro
			 * valore nel Database è di tipo String, avremo bisogno di castare l'oggetto a
			 * String.
			 */
			Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
			// Se l'account che ci ha restituito il metodo leggiPerSessionID è uno staff
			// verremo reindirizzati alla pagina traguardi.html.
			if (a.isStaff())
				ris = "areariservatastaff.html";
			// Altrimenti verremo reindirizzati alla pagina datipersonali.html
			else
				ris = "areariservata.html";
		} // Fine di if
		return ris;
	}// Fine di home

	/*
	 * L'annotation GetMapping è una annotation scorciatoria per evitare di
	 * scrivere @RequestMapping(method = RequestMethod.POST) e gestisce le richieste
	 * POST. Creiamo un metodo a cui accediamo quando riceviamo l'URL
	 * /areariservata/prenota. Questo metodo ci restituisce una String e come
	 * parametro vengono passati la HTTPSession e il corpo di una richiesta POST
	 * come MultiValueMap<String, String>. Usiamo una MultiValueMap poichè è il modo
	 * di immagazzinare i dati del corpo di una richiesta POST di Spring. A
	 * differenze di una Map in Java, questa di Spring potrebbe contenere delle
	 * chiavi con lo stesso valore. L'annotation ResponseBody indica che la risposta
	 * del metodo deve essere legata al corpo della risposta web. Questo metodo
	 * serve per inserire all'interno del Database una prenotazione.
	 */
	@PostMapping("/prenota")
	@ResponseBody
	public String prenotazione(@RequestBody MultiValueMap<String, String> map, HttpSession session) {
		// L'oggetto mappaCompleta avrà inizialmente come coppie chiave/valore datadon,
		// note, idsede, idtipo.
		// Ci manca iddon, che ricaviamo dalla session, per poter inserire queste coppie
		// all'interno di un oggetto di tipo Donazione
		Map<String, String> mappaCompleta = map.toSingleValueMap();

		/*
		 * Le prossime righe servono per ricavare l'iddon dalla session e aggiungerlo
		 * alla mappaCompleta. Creiamo quindi un oggetto a di tipo Donatore attraverso
		 * l'oggetto da di tipo DAOAccount filtrando i risultati per SessionID
		 * attraverso il metodo leggiPerSessionID. Questo metodo ritorna uno specifico
		 * account presente nel Database che ha come valore di sessionID (presente come
		 * colonna nel database nella tabella account) quello preso dalla HTTPSession.
		 * Poichè il metodo getAttribute ci restituisce un oggetto di tipo Session
		 * mentre il nostro valore nel Database è di tipo String, avremo bisogno di
		 * castare l'oggetto a String. Inoltre il medoto ci ritorna un oggetto di tipo
		 * Account, mentre noi abbiamo bisogno di un oggetto di tipo Donatore. Per
		 * questo motivo abbiamo bisogno di castare l'oggetto a Donatore.
		 */
		Donatore dt = (Donatore) da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		// Inseriamo all'interno della mappaCompleta l'unico valore che ci mancava,
		// ovvero l'iddon preso dall'oggetto appena creato dt.
		mappaCompleta.put("iddon", dt.getId() + "");
		// Utilizziamo la mappa ora DAVVERO completa per creare l'oggetto di tipo
		// Donazione.
		Donazione dz = (Donazione) context.getBean("donazioneMappa", mappaCompleta);
		// Se la creazione è andata a buon fine verremo reindirizzati alla pagina /?
		if (dd.creaDonazione(dz)) {
			return "1";
		}
		// Altrimenti ci verrà restituita una String di errore.
		return "0";
	}

	@GetMapping("/eliminaprenotazione")
	@ResponseBody
	public String eliminaPrenotazione(@RequestParam("data") String data, HttpSession session) {
		String ris = "0";
		System.out.println("Prova elimina " + data);
		List<Donazione> donazioni = dd.leggiPerData(data);
		if (donazioni.size() > 0) {
			Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
			Donazione d = donazioni.get(0);
			if (a.isStaff()) {
				if(dd.eliminaDonazione(d.getId()))
						ris = "1";
			}else if(a.getId() == d.getIdDon())
				if(dd.eliminaDonazione(d.getId()))
						ris = "1";
		}
		return ris;
	}

	// Creiamo un metodo a cui accediamo quando riceviamo l'URL
	// /areariservata/prenotazioni che ci restituisce una pagina html ovvero
	// prenotazioni.html.
	@GetMapping("/prenotazioni")
	public String prenotazioni() {
		return "prenotazioni.html";
	}

	// @GetMapping("/donazione") per andare alla pag Donazione
	// @GetMapping("/statistiche") per andare alla pag Statistiche
	// @GetMapping("/contatti") per andare alla pag Contatti
	// @GetMapping("/sedi") per andare alla pag Sedi
	// @GetMapping("/prenotazioni") per andare alla pag Prenotazione
}
