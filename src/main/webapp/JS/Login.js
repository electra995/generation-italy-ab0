/*
Questa documento JS descrive come si comporterà la pagina del login.
Quando il doc html sarà pronto jQuery farà partire la funzione stampaRegioni
e quando verrà cliccato il bottone login partirà la funzione login.
Quando verrà cliccato il bottone registrazione partirà la funzione signup.
*/
$(document).ready(function () {
  stampaRegioni();
  $("#btnLogin").click(function () {
    login();
  });
  $("#btnRegistrazione").click(function(){
    signup();
  })
});
/*
La funzione login manda una richiesta post alla pagina /accesso/login
e passa il valore di email e password ricavato dai campi con il corrispettivo id.
La funzione:
function(){
  document.location.href = "/?";
}
serve per reindirizzare alla pagina index/?
*/
function login() {
  $.post("/accesso/login", {
    email: $("#loginEmail").val(),
    password: $("#loginPassword").val()
  }, function(){
    document.location.href = "/?";
  });
}
/*
La funzione signup serve per iscriversi. Facciamo una richiesta post a /accesso/signup
e mandiamo i valori di una mappa JSON creata per l'occasione.
Si assegna ai vari campi del JSON un nome e il valore del campo html con l'id indicato
(coppia chiave-valore).
*/

function signup(){
  $.post("/accesso/signup",
  {
    nome: $("#nome").val(),
    cognome: $("#cognome").val(),
    sesso: $("#m").is(":checked"),
    dob: $("#dob").val(),
    cell: $("#cell").val(),
    regione: $("#comboRegioni").val(),
    email: $("#email").val(),
    password: $("#password").val(),
    tiposangue: $("#gruppo").val(),
    /*per l'rh controlliamo se rh sconosciuto è selezionato
      se lo è si assegna rh = null, altrimenti si chiede se rhPos
      è selezionato e torna un booleano (true/false)
    */

    rh: $("#rhScon").is(":checked") ? null : $("#rhPos").is(":checked")
  },
    /*
      Quando il server ci manda una risposta si avvia una funzione in base alla risposta stessa
      ci reindirizza a /index/? se va a buon fine altrimenti ci apparirà un alert
      (un pop-up) che ci avviserà
    */
    function(risposta){
    if(risposta == "/?"){
      document.location.href = "/?";
    }else{
      alert("Registrazione non andata a buon fine");
    }
  })
}
/*
La funzione stampaRegioni dichiara una variabile regioni, e manda una richiesta get
a /json/regioni e una funzione che riceve come parametro dataReg . Questa funzione
inizializza regioni assegnandogli un file JSON contenente dataReg parsato a JSON.

*/
//Metodo per costruire e riempire la combobox delle regioni
function stampaRegioni() {
  var regioni;
  $.get("/json/regioni", function (dataReg) {
    regioni = JSON.parse(dataReg);
    /* dichiaro e inizializzo html assegnandogli il codice html
       che indica l'inizio della combobox
       aggiungiamo poi le opzioni
    */
      var html='<label for="regione" class="title">Regione</label>';
      html += "<select id='comboRegioni' class='rettangolo' name='regioni'>";
      //Aggiungiamo un'opzione fissa chiamata "Tutte le regioni" con valore -1
      //(verrà interpretato dal backend e ci darà le sedi di tutte le regioni)
      html += "<option value = -1>Tutte le regioni</option>";
      /*
        faccio un ciclo for, in cui ciclo le regioni e aggiungo le regioni
        nella stringa html
        Per ogni regione creiamo una diversa opzione dentro la combo box
      */
      for (var i in regioni) {
        html += "<option value=" + regioni[i].id + ">" + regioni[i].nome + "</option>";
      }
      html += "</select>"
      //Inseriamo dentro la combo box regioni l'html appena compilato

      $("#boxRegioni").html(html);
  });
}
