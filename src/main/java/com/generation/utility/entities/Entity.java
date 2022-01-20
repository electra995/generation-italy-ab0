package com.generation.utility.entities;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

//Entity sar� la classe base (modello, padre) per tutte le classi dei nostri futuri progetti
public abstract class Entity
{
	//L'unica propriet� di Entity � id
	private int id;
	
	//Entity ha il costruttore vuoto
	public Entity()
	{
		
	}
	
	//Entity ha un costruttore a cui passo id
	public Entity(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * Il metodo toMap() trasforma gli oggetti di tipo Entity in mappe.
	 * Entity � astratta quindi il metodo servir� per trasformare in mappe i figli di Entity.
	 * I nomi delle propriet� saranno le chiavi.
	 * I valori delle chiavi saranno i valori veri e propri delle propriet�
	 * @return una mappa (HASHMAP) corrispondente all'oggetto di tipo Entity
	 */
	public Map<String, String> toMap()
	{
		Map<String, String> ris = new HashMap<String, String>();
		for(Method m : this.getClass().getMethods())
		{
			if(	m.getName().startsWith("get")				&&
				!m.getName().equalsIgnoreCase("getClass")	&&
				m.getParameterCount() == 0					)
				{	
					try
					{
						String key = m.getName().substring(3).toLowerCase();	//key = getId -> Id -> id
						String value = m.invoke(this) + "";						//value = getId();
						ris.put(key, value);
					}
					catch(Exception e)
					{
						System.out.println("Eccezione del metodo toMap() in Entity");
						e.printStackTrace();
					}
				}//Fine di if
			else if(m.getName().startsWith("is"))
			{
				try
				{
					String key = m.getName().substring(2).toLowerCase();
					String value = m.invoke(this) + "";
					ris.put(key, value);
				}
				catch(Exception e)
				{
					System.out.println("Eccezione del metodo toMap() in Entity");
					e.printStackTrace();
				}
			}
		}//Fine di for
		return ris;
	}//Fine di toMap();
	
	public Entity fromMap(Map<String, String> map)
	{
		for(Method m : this.getClass().getMethods())
		{
			if(m.getName().startsWith("set") && m.getParameterCount() == 1)
			{
				//Recupero il nome della propriet� dell'oggetto dal metodo set()
				String nomeProprieta = m.getName().substring(3).toLowerCase();	//setId() -> setId -> Id -> id
				//Verifico che il nome della propriet� appena recuperato
				//corrisponda a una delle chiavi che mi sono state passate come parametro
				if(map.containsKey(nomeProprieta))
				{
					//Se sono qui significa che il nome della propriet� � anche una chiave della mappa
					//Recupero il valore della chiave della mappa e lo assegno a una variabile String valore
					String valore = map.get(nomeProprieta);
					
					//Ora sono in questa situazione
					//nomeProprieta = "id"
					//valore = "1" (il valore 1 � solo a titolo di esempio!)
					
					try
					{
						//String tipo = setId(int id) -> int id -> int -> "int" -> "int"
						String tipo = m.getParameters()[0].getType().getSimpleName().toLowerCase();
						
						switch(tipo)
						{
							//A seconda del tipo (String, int, etc.) andiamo a impostare il valore
							case "int"	:
								m.invoke(this, Integer.parseInt(valore));
								//setId(int 1)
								//chiave = "id" - valore = "1"
								//tipo = "int"
								//Chiedo al programma di eseguire il metodo setId(int 1)
								//Il metodo setId assenger� in questo modo il valore 1
								//al parametro id di tipo int
							break;
							
							case "string"	:
								m.invoke(this, valore);
							break;
							
							case "double"	:
								m.invoke(this, Double.parseDouble(valore));
							break;
							
							case "date"	:
								m.invoke(this, Date.valueOf(valore));
							break;
							
							case "boolean"	:
								//Per il boolean dobbiamo pensae a TUTTI i modi in cui potrebber
								//arrivarci i valori booleani: nel DB ad esempio arrivano
								//come 0 e 1 ma dall'utente arrivano si, no, true, false, etc.
								m.invoke(this, 	valore.equals("1")				||
												valore.equalsIgnoreCase("true")	||
												valore.equalsIgnoreCase("vero")	||
												valore.equalsIgnoreCase("si")	||
												valore.equalsIgnoreCase("s�")	);
							break;
							
							default	:
								//Se il tipo non � tra quelli dello switch lo stampo in console.
								//System.err stampa in rosso invece che in nero!
								System.err.println("Nel fromMap() non ho riconosciuto il tipo " + tipo);
							break;	
						}//Fine di switch
					}
					catch(Exception e)
					{
						System.out.println("Catch del metodo fromMap() di Entity");
						e.printStackTrace();
					}	
				}
			}//Fine di if
		}//Fine di for
		//Alla fine di tutto, facciamo ritornare l'oggetto da cui � partito il metodo fromMap()
		//ma stavolta l'oggetto avr� i valori delle proprit� impostati
		return this;
	}//Fine del metodo fromMap();
	
	public String toString()
	{
		String ris = "id: "	+	id	+"\n";
		Map<String, String> mappa = toMap();
		for(String key : toMap().keySet())
			if(!key.equals("id"))
				ris += key + ": " + mappa.get(key) + "\n";
		ris += "-----------";
		return ris;
	}//Fine di toString();
}