//Questa funzione serve per stampare i dati ricevuti dal Database nei vari tag HTML
//Il dollaro l'elemento con cui accedere e autti i metodi di jQuery.
//.ready è un metodo di JQuery a cui bisogna passare una funzione ed eseguirà quella funzione quando l'elemento HTML specificato è pronto.
$(document).ready(function(){
  //La funzione che gli stiamo passando in questo caso è una richiesta get a cui dobbiamo passare l'URL e una function.
  //Il parametro che noi passiamo nella function sarà la risposta della nostra richiesta get.
    $.get("/json/account", function(data){
      //Var utente conterrà l'oggetto JSON letto dalla risposta del backend (sarà una String che viene trasformata in
      // JSON tramite il metodo .parse).
      var utente = JSON.parse(data);
      //Facciamo una richiesta get a cui passiamo un URL con il parametro id della regione che leggiamo dalla risposta del server ed  esegue una function.
      $.get("/json/regione?id=" + utente.regione, function(nomeRegione){
        //Assegnamo agli elmenti HTML selezionati il valore che viene letto dal Database.
        $("#nome").val(utente.nome);
        $("#cognome").val(utente.cognome);
        //In questo caso se il valore è maschio (1, true) spuntiamo la casella corrispondente a male. Altrimenti spuntiamo quella female.
        utente.sesso ? $("#male").prop("checked", true) : $("#female").prop("checked", true);
        $("#dob").val(utente.dob);
        $("#sangue").val(utente.tipoSangue);
        //Come sopra ma in questo caso con RH positivo o negativo.
        utente.rh ? $("#positivo").prop("checked", true) : $("#negativo").prop("checked", true);
        $("#regione").val(nomeRegione);
        $("#number").val(utente.cell);
        $("#email").val(utente.email);
      })
    })
  });
