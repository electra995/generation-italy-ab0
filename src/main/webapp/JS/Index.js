/*
Questo documento JS riguarda la home page. Serve per indicare alla homepage come
comportarsi quando l'utente clicca sugli elementi interattivi come le frecce
per andare avanti e indietro sulle slide di testo.
Con $(document).ready(function() indichiamo a jQuery di far partire una funzione
quando il documento html ha finito di caricarsi (per sincronizzare il processo).
var contaP è una variabile che indica il numero della slide che stiamo visualizzando.
$("#arrowsx").click(function() invece ci indica che quando clicchiamo sulla
freccia sinistra (che è una immagine del documento html contrassegnata con
l'id arrowsx) parte una funzione.
Questa funzione prende titolo, sottotitolo e testo e li concatena con la variabile
contaP e diciamo che per essi il parametro "hidden" sarà true.
Praticamente nell'HTML abbiamo indicato come nascosti i paragrafi 2,3 e 4 con il
primo paragrafo visibile. Con il nostro click sulla freccia indietro noi rendiamo
visibili gli altri paragrafi. Se siamo sul primo paragrafo andiamo indietro sul
quarto paragrafo, se siamo sul secondo allora passiamo al primo facendo contaP--.
Rendiamo allora il titoloX non nascosto.
*/
$(document).ready(function () {
   var contaP = 1;
   $("#contenitore-arrowsx").click(function () {
      $("#titolo" + contaP).attr("hidden", true);
      $("#sottotitolo" + contaP).attr("hidden", true);
      $("#testo" + contaP).attr("hidden", true);
      if (contaP != 1)
         contaP--;
      else
         contaP = 4;

      $("#titolo" + contaP).attr("hidden", false);
      $("#sottotitolo" + contaP).attr("hidden", false);
      $("#testo" + contaP).attr("hidden", false);
   })

   /*
   Facciamo la stessa cosa con la freccia destra, indicata con l'id "arrowdx", con
   l'unica differenza che faremo ovviamente un incremento positivo per contaP quando
   contaP è diverso da 4.
   */
   $("#contenitore-arrowdx").click(function () {
      $("#titolo" + contaP).attr("hidden", true);
      $("#sottotitolo" + contaP).attr("hidden", true);
      $("#testo" + contaP).attr("hidden", true);
      if (contaP != 4)
         contaP++;
      else
         contaP = 1;

      $("#titolo" + contaP).attr("hidden", false);
      $("#sottotitolo" + contaP).attr("hidden", false);
      $("#testo" + contaP).attr("hidden", false);
   })
})