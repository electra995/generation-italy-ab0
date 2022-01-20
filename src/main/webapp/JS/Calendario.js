let today = new Date();
let currentMonth = today.getMonth();
let currentYear = today.getFullYear();
let selectYear = document.getElementById("year");
let selectMonth = document.getElementById("month");

let months = ["Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"];

let monthAndYear = document.getElementById("monthAndYear");
showCalendar(currentMonth, currentYear);


function next() {
    currentYear = (currentMonth === 11) ? currentYear + 1 : currentYear;
    currentMonth = (currentMonth + 1) % 12;
    showCalendar(currentMonth, currentYear);
}

function previous() {
    currentYear = (currentMonth === 0) ? currentYear - 1 : currentYear;
    currentMonth = (currentMonth === 0) ? 11 : currentMonth - 1;
    showCalendar(currentMonth, currentYear);
}

function jump() {
    currentYear = parseInt(selectYear.value);
    currentMonth = parseInt(selectMonth.value);
    showCalendar(currentMonth, currentYear);
}

function showCalendar(month, year) {

    $.get("/json/donazionimese?mese=" + month + "&anno=" + year, function (ris) {
        var donazioni = JSON.parse(ris);
        let firstDay = (new Date(year, month)).getDay();
        let daysInMonth = 32 - new Date(year, month, 32).getDate();

        let tbl = document.getElementById("calendar-body"); // body of the calendar

        // clearing all previous cells
        tbl.innerHTML = "";

        // filing data about month and in the page via DOM.
        monthAndYear.innerHTML = months[month] + " " + year;
        selectYear.value = year;
        selectMonth.value = month;

        // creating all cells
        let date = 1;
        for (let i = 0; i < 6; i++) {
            // creates a table row
            let row = document.createElement("tr");

            //creating individual cells, filing them up with data.
            for (let j = 0; j < 7; j++) {
                if (i === 0 && j < firstDay) {
                    let cell = document.createElement("td");
                    let cellText = document.createTextNode("");
                    cell.appendChild(cellText);
                    row.appendChild(cell);
                }
                else if (date > daysInMonth) {
                    break;
                }

                else {
                    let cell = document.createElement("td");
                    let cellText = document.createTextNode(date);

                    var donazione = undefined;
                    var mese = month.length == 1 ? "0" + (month + 1) : month + 1;
                    var giorno = date.length == 1 ? ("0" + date) : date;
                    for (var x in donazioni) {
                        var dataDivisa = donazioni[x].dataDon.split("-");
                        if (dataDivisa[0] == year &&
                            dataDivisa[1] == mese &&
                            dataDivisa[2] == giorno) {
                            donazione = donazioni[x]
                            break;

                        }
                    }
                    //var donazione = donazioni.find(d => new Date(d.dataDon) == dataCalendario);
                    if (donazione != undefined) {
                        if (donazione.id > 0) {
                            cell.className += "green";
                            cell.addEventListener("click", function(){
                                var giornoCella = this.innerHTML.length == 1 ? ("0" + this.innerHTML) : this.innerHTML;
                                var meseCella = (month+"").length == 1 ? "0" + (month + 1) : month + 1;
                            donazionePropria(year+"-"+meseCella+"-"+giornoCella)
                            });
                        } else {
                            cell.className += "grey";
                            cell.addEventListener("click", function(){
                                var giornoCella = this.innerHTML.length == 1 ? ("0" + this.innerHTML) : this.innerHTML;
                                var meseCella = (month+"").length == 1 ? "0" + (month + 1) : month + 1;
                            donazioneImpropria(year+"-"+meseCella+"-"+giornoCella)
                            });
                        }
                    }else{
                        cell.className += "white";
                        cell.addEventListener("click", function(){
                            var giornoCella = this.innerHTML.length == 1 ? ("0" + this.innerHTML) : this.innerHTML;
                            var meseCella = (month+"").length == 1 ? "0" + (month + 1) : month + 1;
                            aggiuntaDonazione(year+"-"+meseCella+"-"+giornoCella)});
                    }
                    cell.appendChild(cellText);
                    row.appendChild(cell);
                    date++;
                }

            }

            tbl.appendChild(row); // appending each row into calendar body.
        }
    })
}


function donazioneImpropria(data){
    nascondiCampi();
    modificaCampi(false);
    $("#btnElimina", window.parent.document).hide();
    $("#btnPrenota", window.parent.document).hide();
    $("#data", window.parent.document).val(data);
    alert("Esista già una donazione altrui nel giorno selezionato. Seleziona un'altra data.");
}

function donazionePropria(data){
    mostraCampi();
    modificaCampi(true);
    $("#btnElimina", window.parent.document).show();
    $("#btnPrenota", window.parent.document).hide();
    $("#data", window.parent.document).val(data);
    $.get("/json/controllavisualizzazionedonazione?data=" + data, function(donString){
        var don = JSON.parse(donString);
        if(don.id != -1){
            $("#comboSedi", window.parent.document).val(""+don.idSede);
            $("#comboTipi", window.parent.document).val(""+don.idTipo);
            $("#note", window.parent.document).val(don.note);
        }
    })
}

function aggiuntaDonazione(data){
    nascondiCampi();
    mostraCampi();
    svuotaCampi();
    modificaCampi(false);
    $("#btnElimina", window.parent.document).hide();
    $("#btnPrenota", window.parent.document).show();
    $("#data", window.parent.document).val(data);
}

function mostraCampi(){
    $("#boxSedi", window.parent.document).show();
    $("#boxTipi", window.parent.document).show();
    $("#boxNote", window.parent.document).show();
}

function nascondiCampi(){
    $("#boxSedi", window.parent.document).hide();
    $("#boxTipi", window.parent.document).hide();
    $("#boxNote", window.parent.document).hide();
    $("#btnElimina", window.parent.document).hide();
}

function modificaCampi(bool){
    $("#comboSedi", window.parent.document).prop("disabled",bool);
    $("#comboTipi", window.parent.document).prop("disabled",bool);
    $("#note", window.parent.document).prop("readonly",bool);
}

function svuotaCampi(){
    $("#data", window.parent.document).val("");
    $("#comboSedi", window.parent.document).val("");
    $("#comboTipi", window.parent.document).val("");
    $("#note", window.parent.document).val("");
}

function resetCampi(){
    //Svuota campi
    $("#data", window.parent.document).val("");
    $("#comboSedi", window.parent.document).val("");
    $("#comboTipi", window.parent.document).val("");
    $("#note", window.parent.document).val("");
    //Nascondi campi
    $("#boxSedi", window.parent.document).hide();
    $("#boxTipi", window.parent.document).hide();
    $("#boxNote", window.parent.document).hide();
    $("#btnPrenota", window.parent.document).hide();
    $("#btnElimina", window.parent.document).hide();
}