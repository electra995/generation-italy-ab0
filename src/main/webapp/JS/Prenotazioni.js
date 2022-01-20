$(document).ready(function () {
  $("#btnPrenota").click(function () {
    prenota();
  });
  $("#btnElimina").click(function () {
    eliminaPrenotazione();
  });
  stampaSedi();
  stampaTipi();
});

function controllaVisualizzazione() {
  $.get("/json/controllavisualizzazionedonazione?data=" + $("#dataInput").val(), function (risposta) {
    var donazione = JSON.parse(risposta);
    switch (donazione.id) {
      case -1:
        $("#prenotazione").val("Non hai i permessi di visualizzare questa donazione");
        break;
      case -2:
        stampaSedi();
        stampaTipi();
        $("#prenotazione").val("Non esiste nessuna donazione nella data selezionata");
        break;
      default:
        $("#prenotazione").val("Donatore: " + donazione.idDon + " - Sede: " + donazione.idSede +
          " - Tipo donazione: " + donazione.idTipo + " - ID Donazione: " + donazione.id);
        break;
    }
  });
}

//IN SOSPESO PERCHè NON SO FARE  UN PIRIBICCHIO
//function controllaPrenotazione() {
//  $.get("/json/controllaprenotazionedonazione?data="+$("#dataInput").val(),
//  },
//}

//DOVREBBE ANDARE DENTRO CONTROLLA PRENOTAZIONE FORSE  E DEVONO ESSERE PASSATI I DATI GIUSTI
function prenota() {
  $.post("/areariservata/prenota",
    {
      datadon: $("#data").val(),
      note: $("#note").val(),
      idsede: $("#comboSedi").val(),
      idtipo: $("#comboTipi").val()
    }, function (risposta) {
      if (risposta == "1") {
        resetCampi();
      } else {
        alert("Errore nella prenotazione");
      }
    })
}

function eliminaPrenotazione(){
  $.get("/areariservata/eliminaprenotazione?data=" + $("#data").val(), function(risposta){
      if(risposta == "1"){
        resetCampi();
      }else{
        alert("Errore nella rimozione della prenotazione.");
      }
  })
}

function resetCampi(){
  //Svuota campi
  $("#data").val("");
  $("#data").trigger("change");
  $("#comboSedi").val("");
  $("#comboTipi").val("");
  $("#note").val("");
  //Nascondi campi
  $("#boxSedi").hide();
  $("#boxTipi").hide();
  $("#boxNote").hide();
  $("#btnPrenota").hide();
  $("#btnElimina").hide();
  $("#icalendario").prop("src","");
  $("#icalendario").prop("src","calendario.html");
}


//IN TEORIA DOVREBBE FUNZIONARE
function stampaSedi() {
  var sedi;
  $.get("/json/sedi?idregione=-1", function (sediJson) {
    sedi = JSON.parse(sediJson);
    var html = "<select class =\"rettangolo\" id=\"comboSedi\" name='sedi'>";
    html += "<option value=-1> Seleziona una sede</option>";
    for (var i in sedi) {
      html += "<option value=" + sedi[i].id + ">" + sedi[i].nome + "</option>";
    }
    html += "</select>"
    $("#boxSedi").html(html);
  });
}

//IN TEORIA DOVREBBE FUNZIONARE
function stampaTipi() {
  var tipi;
  $.get("/json/tipi", function (tipiJson) {
    tipi = JSON.parse(tipiJson);
    var html = "<select id=\"comboTipi\" name='tipi'>";
    html += "<option value=-1> Seleziona il tipo di donazione</option>";
    for (var i in tipi) {
      html += "<option value=" + tipi[i].id + ">" + tipi[i].nome + "</option>";
    }
    html += "</select>"
    $("#boxTipi").html(html);
  });
}
