//Con document.ready impostiamo una funziona da eseguire appena carica l'HTML
$(document).ready(function () {
  //Chiamiamo i metodi di stampa delle Regioni e Sedi
  stampaRegioni();
  stampaSedi();
});

//Metodo per costruire e riempire una tabella contenente le regioni lette dal DB
function stampaSedi() {
  var regioni;
  var sedi;
  //Eseguiamo una richiesta GET all'indirizzo /json/regioni
  ///json contiene tutti i mapping relativi alla richiesta di dati
  //Dentro la function che verrà eseguita quando viene ricevuta una risposta dal server
  //passiamo la variabile dataReg, che conterrà la risposta del server
  $.get("/json/regioni", function (dataReg) {
	  //Tramite JSON.parse trasformiamo la risposta (JSON sottoforma di String) in oggetto JSON
    regioni = JSON.parse(dataReg);
	//Eseguiamo una richiesta GET al controller dati /sedi a cui passiamo
	//un parametro chiamato idregione che prende il valore contenuto dentro il comboRegioni
	//Inserendo la risposta del server dentro la variabile dataSedi
    $.get("/json/sedi?idregione="+$("#comboRegioni").val(), function (dataSedi) {
		//Trasformiamo la risposta del server (JSON sottoforma di String) in oggetto JSON
      sedi = JSON.parse(dataSedi);
      var html = "<table><thead><tr><th>Regione</th><th>Nome Sede</th></tr></thead><tbody>";
      for (var i in sedi) {
        //Tramite find (find cicla per tutte le regioni e restituisce la prima regione
		//che soddisfa la condizione (r.id == sedi[i].idRegione)
		//prendiamo la regione il cui id coincide con la sede iesima
        var regione = regioni.find(r => r.id == sedi[i].idRegione);
		//Aggiungiamo per ogni sede una riga dentro la tabella
        html += "<tr>";
		//Inseriamo dentro la riga iesima due dati:
		//il nome della regione (regione.nome)
		//il nome della sede (sed[i].nome)
        html += "<td>" + regione.nome + "</td>";
        html += "<td id='tdSede'>" + sedi[i].nome + "</td>";
        //Chiudiamo la riga iesima
        html += "</tr>";
      }
      //Dopo aver inserito tutte le sedi dentro la tabella, la chiudiamo
      html += "</tbody></table>";
      //Utilizziamo l'html appena compilato dentro tabellaSedi
      $("#tabellaSedi").html(html);
      console.log(html);
    });
  });
}

//Metodo per costruire e riempire la combobox delle regioni
function stampaRegioni() {
  var regioni;
  //Eseguiamo una richiesta get al mapping /regioni dentro /json
  //a cui assocciamo una function contenente la risposta del server (dataReg)
  $.get("/json/regioni", function (dataReg) {
	//Trasformiamo la risposta del server (JSON sottoforma di String) in oggetto JSON
    regioni = JSON.parse(dataReg);
	  //Istanziamo una variabile con:
	  //Apertura combobox (<select> con id predefinito
	  //Colonne tabelle <th> contenenti "Regione" e "Nome Sede"
      var html = "<select id=\"comboRegioni\" name='regioni'>";
	  //Aggiungiamo un'opzione fissa chiamata "Tutte le regioni" con valore -1
	  //(verrà interpretato dal backend e ci darà le sedi di tutte le regioni)
      html += "<option value = -1>Tutte le regioni</option>";
	  //Per ogni regione creiamo una diversa opzione dentro la combo box
      for (var i in regioni) {
		  //Ogni option avrà il nome della reigone ed il proprio id letti dalla regione iesima
        html += "<option value=" + regioni[i].id + ">" + regioni[i].nome + "</option>";
      }
	  //Dopo ave rinserito tutte le regioni chiudiamo la combo box
      html += "</select>"
	  //Inseriamo dentro la combo box regioni l'html appena compilato
      $("#boxRegioni").html(html);

		//Impostiamo una funzione che si avverrà ogni qual volta la selezione di comboregioni cambierà
      $("#comboRegioni").change( function(){
		//Essa ricaricherà le sedi con la regione che abbiamo selezionato
        stampaSedi();
      })
  });
}
