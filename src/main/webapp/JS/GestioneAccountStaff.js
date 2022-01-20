$(document).ready(function () {
	stampaTabella();
});
function stampaTabella() {
	$.get("/json/leggiaccountstaff", function (ris) {
		var utenti = JSON.parse(ris);
		var html = "<table><thead><tr><th>Id</th><th>Nome</th><th>Cognome</th><th>Sesso</th><th>Data di nascita</th>"
		html += "<th>Cellulare</th><th>Gruppo sanguigno</th><th>Email</th><th>Elimina</th></tr>";
		html += "</thead><tbody>";
		utenti.forEach(utente => {
			html += "<tr>"
			html += "<td>" + utente.id + "</td>";
			html += "<td>" + utente.nome + "</td>";
			html += "<td>" + utente.cognome + "</td>";
			html += "<td>" + (utente.sesso ? "M" : "F") + "</td>";
			html += "<td>" + utente.dob + "</td>";
			html += "<td>" + utente.cell + "</td>";
			html += "<td>" + utente.tipoSangue + (utente.rh ? "+" : "-") + "</td>";
			html += "<td>" + utente.email + "</td>";
			html += "<td> <div id='ics' onclick='eliminaAccount(\"" + utente.email + "\")'><img src='../Immagini/delete.png' width='15' class='icslogo'></div></td>";
			html += "</tr>"
		});
		html += "</tbody> </table>";
		$("#boxUtenti").html(html);
	})
};

function eliminaAccount(email) {
	$.post("/json/eliminaaccount?email=" + email, function (risposta) {
		if (risposta) {
			stampaTabella();
			alert("Account eliminato correttamente");
		}
		else {
			alert("Errore");
		}

	})
};
