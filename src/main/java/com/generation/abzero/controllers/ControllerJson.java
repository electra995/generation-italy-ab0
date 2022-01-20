package com.generation.abzero.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.generation.abzero.dao.DAOAccount;
import com.generation.abzero.dao.DAODonatori;
import com.generation.abzero.dao.DAODonazioni;
import com.generation.abzero.dao.DAORegioni;
import com.generation.abzero.dao.DAOSedi;
import com.generation.abzero.dao.DAOTipi;
import com.generation.abzero.entities.Account;
import com.generation.abzero.entities.Donatore;
import com.generation.abzero.entities.Donazione;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//Annotation che riceve delle request e spedisce delle response
@Controller
@RequestMapping("/json")
public class ControllerJson {
	// Annotation che prende tutte le proprietà di classe di DAOTipi e le inietta
	// nell'oggetto dt
	@Autowired
	DAOTipi dt;

	@Autowired
	DAOSedi ds;

	@Autowired
	DAORegioni dr;

	@Autowired
	DAOAccount da;

	@Autowired
	DAODonazioni dd;

	@Autowired
	DAODonatori dad;

	// Creiamo un oggetto del tipo Gson a cui passiamo un format data da utilizzare
	// Così gestiamo le date che ci arrivano negli oggetti da trasformare secondo il
	// format anno-mese-giorno
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	// IN TEORIA HO COLLEGATO QUESTO URL AL COMBO BOX PER STAMPARE LE SEDI NELLA
	// PAGINA DELLE PRENOTAZIONI
	// Se l'Id della regione è -1 restituisce una lista di tutte le sedi sottoforma
	// di String in formato Json
	// Altrimenti con l'id specifico restituisce una lista di sedi selezionate per
	// regione
	// @RequestParam("idregione") cercherà nell'indirizzo un parametro chiamato
	// "idregione"
	// e lo associerà alla variabile int idReg.
	// Es. localhost:8080/json/sedi?idregione=4 = idReg=4
	@GetMapping("/sedi")
	@ResponseBody
	public String stampaSedi(@RequestParam("idregione") int idReg) {
		String ris;
		if (idReg == -1) {
			// Tramite il metodo gson.toJson trasformiamo la lista di sedi ottenute dal
			// DAOSedi
			// in una Stringa formattata Json.
			ris = gson.toJson(ds.leggiTutti());
		} else {
			ris = gson.toJson(ds.leggiPerRegione(idReg));
		}
		return ris;
	}

	@GetMapping("/welcomeMessagge")
	@ResponseBody
	public String stampaMessaggio(HttpSession session) {
		String ris = "{\"tipo\":0}";// tipo 0 -> Non loggato
		if (session.getAttribute("sessionID") != null) {
			Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));

			if (!a.isStaff())
				// tipo 1 -> donatore, nome -> nome donatore
				ris = "{\"tipo\":1, \"nome\": \"" + ((Donatore) a).getNome() + "\"}";
			else
				ris = "{\"tipo\":2}";
		}
		return ris;
	}

	// Questo metodo restituisce tutte le regioni lette dal db sottoforma di String
	// in formato Json
	@GetMapping("/regioni")
	@ResponseBody
	public String stampaRegioni() {
		return gson.toJson(dr.leggiTutti());
	}

	// localhost:8080/json/regione?id=9
	// Questo metodo stampa il nome della regione che ha come id quello passato per
	// parametro
	@GetMapping("/regione")
	@ResponseBody
	public String stampaRegione(@RequestParam("id") int idRegione) {
		return dr.leggiPerId(idRegione).getNome();
	}

	// DOVREBBE ESSERE UNA PAGINA IN CUI PRENDERE I TIPI DI DONAZIONE DA STAMPARE IL
	// COMBO BOX CHE SI TROVA NELLA PRENOTAZIONE
	// Questo metodo restituisce tutti i tipi di donazione letti dal db sottoforma
	// di String in formato Json
	@GetMapping("/tipi")
	@ResponseBody
	public String stampaTipi() {
		return gson.toJson(dt.leggiTutti());
	}

	// Questo metodo ci ritorna un oggetto di tipo account letto dal db tramite il
	// sessionID
	// associato alla sua sessione con tutte le sue informazioni.
	@GetMapping("/account")
	@ResponseBody
	public String stampaAccount(HttpSession session) {
		Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		// IN MEMORY OF GSON CHRIST
		// JsonObject jo =
		// gson.toJsonTree(a).getAsJsonObject().remove("regione").getAsJsonObject();
		// jo.addProperty("regione", "NON FUNZIA");
		return gson.toJson(a);
	}

	@GetMapping("/leggiaccountstaff")
	@ResponseBody
	public String stampaAccountDonatori(HttpSession session) {
		String ris = "";
		Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		if (a.isStaff()) {
			ris = gson.toJson(da.leggiDonatori());
		}
		return ris;
	}

	@PostMapping("/email")
	@ResponseBody
	public String stampaEmail(HttpSession session) {
		Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		// IN MEMORY OF GSON CHRIST
		// JsonObject jo =
		// gson.toJsonTree(a).getAsJsonObject().remove("regione").getAsJsonObject();
		// jo.addProperty("regione", "NON FUNZIA");
		return a.getEmail();
	}

	@GetMapping("/donazionimese")
	@ResponseBody
	public String stampaDonazioniPerMese(@RequestParam("mese") int mese, @RequestParam("anno") int anno,
			HttpSession session) {
		Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		List<Donazione> donazioniDB = dd.leggiPerMese(mese, anno);

		if (a.isStaff())
			return gson.toJson(donazioniDB);

		List<Donazione> donazioniFiltrate = new ArrayList<Donazione>();
		for (Donazione donazione : donazioniDB) {
			if (donazione.getIdDon() == a.getId()) {
				donazioniFiltrate.add(donazione);
			} else {
				Donazione pseudoDon = new Donazione();
				pseudoDon.setId(-1);
				pseudoDon.setDataDon(donazione.getDataDon());
				donazioniFiltrate.add(pseudoDon);
			}
		}
		return gson.toJson(donazioniFiltrate);
	}

	// HO CAMBIATO NOME A QUESTO URL, NON SO SE HO ROTTO QUALCOSA
	@GetMapping("/controllavisualizzazionedonazione")
	@ResponseBody
	public String stampaDonazione(@RequestParam("data") String data, HttpSession session) {
		// Creo una lista di Donazioni che conterrà le donazioni filtrate per la data
		// ricevuta da JS
		// e quindi che ha inserito l'utente nel calendario (front-end).
		// Poiché nel db per una data corrisponde una e una sola donazione,
		// la lista di donazioniconterrà un solo valore.
		String ris = "";
		List<Donazione> ld = dd.leggiPerData(data);
		// Se c'è una donazioni in quella data
		if (ld.size() > 0) {
			// Creo un oggetto account, a cui passo la SUA specifica sessionID
			Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
			// Creo un oggetto donazione, prendendo il primo (e unico) valore della lista
			Donazione d = ld.get(0);
			// Adesso faccio il controllo, controllando se l'account che sta facendo questa
			// donazione è membro dello staff oppure se è donatore
			// Se è membro dello staff ritorna i dati di tutte le donazioni di quel giorno.
			if (a.isStaff()) {
				ris = gson.toJson(d);
			}
			// Se è un donatore e quella donazione è la sua stampa i dati di quella
			// donazione di quel giorno.
			else if (d.getIdDon() == ((Donatore) a).getId()) {
				ris = gson.toJson(d);
			}
			// Altrimenti, se c'è una donazione ma non è dell'utente che ne fa richiesta,
			// stamperà: "Non hai i permessi di visualizzare questa donazione".
			else
				ris = "{\"id\":-1}";// {"id":-1}
		}
		// Altrimenti, di default, se non c'è nessuna donazione stamperà "Non esiste
		// nessuna donazione nella data selezionata".
		else
			ris = "{\"id\":-2}";// {"id":-2}
		return ris;
	}

	// FORSE HO COMBINATO QUALCHE GUAIO CAMBIANDO QUESTO URL
	// PER IL MOMENTO NON LA USIAMO, è INUTILE
	// Questo metodo ritorna alla pagina prenotazioni.html di areariservata
	@GetMapping("/controllaprenotazionedonazione")
	public String prenotazioni() {
		return "/areariservata/prenotazioni.html";
	}

	@GetMapping("ndonazioni")
	@ResponseBody
	public String numeroDonazioni(HttpSession session) {
		Donatore d = (Donatore) da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		if (d.getId() == 8) {
			return 150 + "";
		}
		List<Donazione> donazioni = dd.leggiDonazioniPassatePerDonatore(d.getId());
		return "" + donazioni.size();
	}

	// QUESTA ROBA NON FINITA DOVREBBE SERVIRE COME BASE PER CONTROLLARE CHE IL
	// DONATORE NON SIA SCADUTO O CHE NON ABBIA MAI EFFETTUATO UNA DONAZIONE
	@GetMapping("/aggiungiprenotazione")
	@ResponseBody
	public String aggiungiPrenotazione(@RequestParam("data") String data, @RequestParam("id") String id,
			HttpSession session) {
		String ris = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat(data);
		Date dataPrenotazione;
		try {
			dataPrenotazione = (Date) sdf1.parse(data);
			List<Donazione> ld = dd.leggiUltimaDonazione(Integer.parseInt(id));
			Donazione d = ld.get(0);
			Date dataUltimaDonazione = d.getDataDon();
			long delta = dataPrenotazione.getTime() - dataUltimaDonazione.getTime();
			delta = delta / (1000 * 60 * 60 * 24);
			if (delta > 730 || d == null)
				ris += "Devi prenotare necessariamente la Donazione differita";
			else
				ris = "/prenota";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ris;
	}

	/*
	 * Questo metodo permette a un membro dello staff di eliminare l'account di un
	 * donatore previa richiesta. Passiamo al metodo la sessione e l'e-mail che
	 * identifica univocamente l'account del donatore. Chiediamo al database di fare
	 * la query "DELETE from Account where email = ?" tramite il metodo
	 * eliminaAccount di DAOAccount
	 * 
	 */
	@PostMapping("/eliminaaccount")
	@ResponseBody
	public boolean eliminaAccount(HttpSession session, @RequestParam("email") String email) {
		Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		Donatore d = dad.leggiPerEmail(email);
		if (a.isStaff()) {
			return  da.eliminaAccount(email);
									// "0";
		} else {
			return false;
		}

	}

}
