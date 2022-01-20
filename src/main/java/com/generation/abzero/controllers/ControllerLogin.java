package com.generation.abzero.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.generation.abzero.dao.DAOAccount;
import com.generation.abzero.dao.DAODonatori;
import com.generation.abzero.dao.DAOStaff;
import com.generation.abzero.entities.*;
//@Controller indica a spring che questa classe avrà dei mapping (mapping = ricerca degli URL)
//@RequestMapping indica che per far accedere ai mapping serve accedere al sito contenente "/accesso" nella parte seguente all'indirizzo di base
@Controller
@RequestMapping("/accesso")
public class ControllerLogin {
	/* @Autowired serve per far istanziare un oggetto, in questo caso "da" di tipo "DAOAccount".
	 * @Autowired cerca l'annotazione "@Bean" nella classe Context che contiene l'istanziazione dell'oggetto.
	 */
	@Autowired
	DAOAccount da;
	
	@Autowired
	DAODonatori dd;

	@Autowired
	DAOStaff ds;
	//ApplicationContex serve per poter usare i bean manualmente. Context contiene le istruzioni su come creare i bean
	@Autowired
	ApplicationContext context;
	
	/* Metodo per testare le sessioni e l'accesso ai vari dati
	 * @GetMapping chiede a Spring di portare la richiesta al metodo associato al mapping quando si incontra /accesso/test
	 * @ResponseBody indica che il metodo scritto sotto dovrà dare una risposta (un return) che non sia una pagina web
	 */
	
	@GetMapping("/test")
	@ResponseBody
	public String testSessione(HttpSession session)
	{
		/* usiamo un "UUID" cioè un identificativo univoco universale. Generiamo un UUID diverso ogni volta che 
		 * l'utente fa login e questo identificativo verrà usato finché l'utente non farà logout.
		 * Http session serve per leggere l'UUID dell'utente.
		 * Creiamo una stringa chiamata uuid e la poniamo uguale al valore dell'elemento sessionID castato a stringa
		 * cui accediamo tramite il metodo getAttribute di session
		 */
		String uuid = (String) session.getAttribute("sessionID");
		//pongo ris uguale alla stringa uuid appena inizializzata
		String ris = uuid;
		//creo l'oggetto a di classe Account che corrisponderà all'oggetto da con lo stesso uuid letto tramite il metodo leggiPerSessionID
		//contenuto nella classe DAOAccount
		Account a = da.leggiPerSessionID(uuid);
		//stampo a video le informazioni di account tramite il metodo toString degli oggetti di classe Entity
		System.out.println(a.toString());
		/* Se a non è membro dello staff (quindi è di tipo concreto donatore)
		 * creo un oggetto di tipo Donatore con i dati di Account e stampo l'uuid e i dati del donatore */
		if (!a.isStaff())
		{
			Donatore d = (Donatore) a;
			ris += "I tuoi dati: " + d.toString();
		}
		/* Altrimenti se è membro dello staff faccio lo stesso
		 * e stampo a video l'uuid e l'id della sede in cui a lavora
		 */
		else
		{
			Staff s = (Staff) a;
			ris += "L'id della tua sede lavorativa è: " + s.getIdSede();
		}
		System.out.println(ris);
		return ris;
	}
	/* @PostMapping indica una richiesta HTTP Post quando si arriva all'url terminante in /signup
	 * @RequestBody indica che il parametro seguente conterrà il corpo della richiesta 
	 * Il metodo registrazione ritorna una stringa e prende come parametro una mappa multivalore e una sessione http
	 * Viene creato un oggetto di classe Account ricavando il bean dal context e passando come parametri
	 * la stringa "donatoriMappa" (che è il nome stesso del bean)
	 * e la multivalue map trasformata in Map<String, String> tramite il metodo toSingleValueMap
	 * Viene settato come non di tipo staff (poiché i membri dello staff non si registrano sul sito)
	 * Se l'account è stato creato con successo allora vieni automaticamente loggato e vieni
	 * reindirizzato alla home page, altrimenti ritorniamo la stringa di avviso
	 */
	@PostMapping("/signup")
	@ResponseBody
	public String registrazione(@RequestBody MultiValueMap<String, String> map,  
								HttpSession session)
	{
		Account a = (Account) context.getBean("donatoriMappa", map.toSingleValueMap());
		a.setStaff(false);
		if(da.creaAccount(a) && dd.creaDonatore((Donatore) a))
		{
			eseguiLogin(a.getEmail(), session);
			return "/?";
		}
		return "Errore nella registrazione";
	}
	/* @ResponseStatus indica nella risposta se la request è andata a buon fine o meno 
	 * Il metodo accesso non ritorna nessun valore ma permette di eseguire il login
	 * Riceve come parametri la mappa multivalore e la sessione HTTP
	 * Crea l'account "a" leggendo e-mail e password, se "a" esiste esegue il login con il metodo eseguiLogin
	 */
	@PostMapping("/login")
	@ResponseStatus(value = HttpStatus.OK)
	public void accesso(@RequestBody MultiValueMap<String, String> map,
							HttpSession session)
	{
		Account a = da.leggiPerCredenziali(map.getFirst("email"), map.getFirst("password"));
		if (a != null)
			eseguiLogin(a.getEmail(), session);
	}
	/* Il metodo eseguiLogin è anch'esso void, riceve come parametri la stringa email e la sessione HTTP.
	 * Crea la stringa sessionID con un metodo standard della libreria java.util e lo assegna alla sessione HTTP.
	 * Infine aggiorna l'account con il metodo impostaSessionID di DAOAccount
	 */
	private void eseguiLogin(String email, HttpSession session) {
		String sessionID = UUID.randomUUID().toString();
		session.setAttribute("sessionID", sessionID);
		//response.addCookie(new Cookie("sessionID", sessionID));
		da.impostaSessionID(email, sessionID);
	}
	/* Il metodo logout ritorna una stringa che indica se il logout è avvenuto.
	 * Se sei loggato chiama il metodo eliminaSessionID passandogli il valore di sessionID
	 * e poi lo rimuove. Se non hai precedentemente effettuato l'accesso si limita a indicartelo.
	 */
	@GetMapping("/logout")
	@ResponseBody
	public String logout(HttpSession session)
	{
		String ris = "Logout effetuato con successo";
		if(session.getAttribute("sessionID") != null)
		{
			da.eliminaSessionID((String) session.getAttribute("sessionID"));
			session.removeAttribute("sessionID");
		}
		else
			ris = "Non hai fatto l'accesso";
		return ris;
	}
	
	@PostMapping("/modificapassword")
	@ResponseBody
	public String modificaPassword(@RequestBody MultiValueMap<String, String> map,
									HttpSession session) {
		Account a = da.leggiPerSessionID((String) session.getAttribute("sessionID"));
		a.setPassword(map.getFirst("password"));
		return da.aggiornaPassword(a) ? "1" : "0";
	}
	
	//Il metodo home ti riporta alla pagina dell'accesso se aggiungi uno slash alla fine dell'URL.
	@GetMapping("/")
	public String home()
	{
		return "login.html";
	}

}
