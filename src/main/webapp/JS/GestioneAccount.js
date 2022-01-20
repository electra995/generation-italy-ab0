$(document).ready(function()
{
	$.post("/json/email", function(email){
		$("#email").val(email);
	});
	
	$("#logout").click(function()
	{
		$.get("/accesso/logout", function(){
			window.parent.location.href = "/?";
		})
	});

	$("#modificapassword").click(function()
	{
		$("#sparisci").slideToggle("slow");
	});

	$("#inviopass").click(function()
	{
		var passInserita = $("#password").val();
		if(passInserita != ""){
			$.post("/accesso/modificapassword", {"password": passInserita}, function(risp){
				alert(risp == "1" ? "La password è stata modificata con successo." : "Errore nella modifica della password.");
			})
		}
	});
});
