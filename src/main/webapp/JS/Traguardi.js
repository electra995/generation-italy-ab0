$(document).ready(function () {

    var list = document.getElementById('progress'),
        children = list.children;

    $.get("/json/ndonazioni", function (ndon) {
        var completed = 0;
        if (ndon >= 100) {
            completed = 7;
        } else if (ndon >= 50) {
            completed = 5;
        } else if (ndon >= 25) {
            completed = 3;
        } else if (ndon >= 1) {
            completed = 1;
        }
        $("#prenotazioniUtente").html("Hai fatto " + ndon + " donazioni!");

        if (completed > children.length) return;

        // for each node that is completed, reflect the status
        // and show a green color!
        for (var i = 0; i < completed; i++) {
            children[i].children[0].classList.remove('grey');
            children[i].children[0].classList.add('verdino');

            // if this child is a node and not divider,
            // make it shine a little more
            if (i % 2 !== 0) {
                children[i].children[0].classList.add('activated');
            }
        }
        });

    });
