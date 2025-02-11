package net.myself.DemoLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/*
* I bean vengono creati e gestiti dal contenitore di oggetti quando sono annotati con le annotazioni appropriate,
* come @Component, @Service, @Repository o @Controller.
* Se hai bisogno di configurare i bean in modo più specifico, puoi utilizzare annotazioni come
* @Configuration e definire metodi con @Bean per registrare i bean manualmente.
*
* Se una classe ha bisogno di un altro bean, puoi specificarlo tramite costruttori o metodi di impostazione annotati con @Autowired.
*
* import org.springframework.context.ApplicationContext;
* import org.springframework.stereotype.Service;
* Utilizzando la classe ApplicationContextProvider:
* Puoi creare una classe di utilità per accedere all'ApplicationContext da qualsiasi punto dell'applicazione.
*
* Quali classi hanno bisogno di essere gestite da Spring?
* - Chi ha bisogno di ricevere dipendenze tramite Dependency Injection.
* - Chi richiede una gestione del ciclo di vita (creazione, distruzione) da parte di Spring.
* - Chi svolge una funzione specifica all'interno dell'architettura dell'applicazione (servizi, repository, controller, ecc.).
*
* */

@SpringBootApplication
@EnableCaching
public class DemoLibraryApplication
{
	public static void main(String[] args) {
		SpringApplication.run(DemoLibraryApplication.class, args);
	}

}
