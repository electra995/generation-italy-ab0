$(document).ready(function () {
    $.get("/json/welcomeMessagge",function(rispostaString) {
        console.log(rispostaString);
        var risposta = JSON.parse(rispostaString);
        switch(risposta.tipo) {
            // areaAccesso - href - window.parent.location.href = /accesso/?
            //imgAccesso  - Immagini/encrypted.png
            case 0:
                $("#areaAccesso").click(function(){window.parent.location.href = "/accesso/?";});
                $("#imgAccesso").attr("src", "Immagini/password.png");
                break;
            case 1:
                $("#areaAccesso").click(function(){window.parent.location.href = "/areariservata/?";});
                $("#imgAccesso").attr("src", "Immagini/donatore.png");
                break;
            case 2:
                $("#areaAccesso").click(function(){window.parent.location.href = "/areariservata/?";});
                $("#imgAccesso").attr("src", "Immagini/staff.png");
                break;
        }
    });
});
