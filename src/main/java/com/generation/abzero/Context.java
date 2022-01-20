package com.generation.abzero;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.generation.abzero.dao.*;
import com.generation.abzero.entities.*;
import com.generation.utility.dao.Database;

@Configuration
public class Context 
{
	//I @Bean specificano a Spring come istanziare degli oggetti 
	@Bean
	@Scope("singleton")
	public Database db() 
	{
		return new Database("bb9wfk8pskstmut0", "my4a9iazd2bikcbb", "jfkcjcbrbu53titf");   
	}
	/* @Scope specifica lo scopo del bean. Prototype significa che verranno create delle copie dello
	 * stesso oggetto (pattern creazionale detto "factory"); possono variare in base ai parametri. 
	 * Singleton invece crea l'oggetto una singola volta e viene richiamato lo stesso oggetto 
	 * ogni volta che viene chiesto quel bean. Nel caso singleton non è obbligatorio scriverlo.
	 * Il metodo tipoMappa riceve una mappa contenente chiave e valore di tipo stringa 
	 * e restituisce un oggetto di classe Tipo.
	 */
	@Bean
	@Scope("prototype")
	public Tipo tipoMappa(Map<String, String> mappa)      
	{
		//Dichiaro e instanzio un oggetto di classe Tipo
		Tipo t = new Tipo();
		//Lo riempio con il contenuto della mappa passata come parametro usando il metodo fromMap
		t.fromMap(mappa);
		return t;
	}
	
	@Bean
	public DAOTipi daoTipi() 
	{
		return new DAOTipi();
	}
	
	@Bean
	@Scope("prototype")
	public Donazione donazioneMappa(Map<String, String> mappa)
	{
	    Donazione d = new Donazione();
	    d.fromMap(mappa);
	    return d;
	}

	@Bean
	public DAODonazioni daoDonazioni()
	{
	    return new DAODonazioni();
	}
	
	@Bean
    @Scope("prototype")
    public Regione regioneMappa(Map<String,String> mappa)
    {
        Regione r = new Regione();
        r.fromMap(mappa);
        return r;
    }

    @Bean
    public DAORegioni daoRegioni()
    {
        return new DAORegioni();
    }


    @Bean
    @Scope("prototype")
    public Staff staffMappa(Map<String,String> mappa)
    {
        Staff s = new Staff();
        s.fromMap(mappa);
        return s;
    }

    @Bean
    public DAOStaff daoStaff()
    {
        return new DAOStaff();
    }

    @Bean
    @Scope("prototype")
    public Account accountMappa(Map<String,String> mappa)
    {   	
    	Account a = (mappa.get("staff").equalsIgnoreCase("true")) ? new Staff() : new Donatore();
    	a.fromMap(mappa);
    	return a;
    }

    @Bean
    public DAOAccount daoAccount()
    {
        return new DAOAccount();
    }
    
    @Bean
    @Scope("prototype")
    public Donatore donatoriMappa(Map<String, String> mappa)
    {
    	Donatore d = new Donatore();
        d.fromMap(mappa);
        return d;
    }

    @Bean
    public DAODonatori daoDonatori()
    {
        return new DAODonatori();
    }
    
    @Bean
    @Scope("prototype")
    public Sede sedeMappa(Map<String, String> mappa)
    {
        Sede s = new Sede();
        s.fromMap(mappa);
        return s;
    }

    @Bean
    public DAOSedi daoSedi()
    {
        return new DAOSedi();
    }
    
}
