package com.alexsaraiva.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.alexsaraiva.cursomc.services.DBService;
import com.alexsaraiva.cursomc.services.EmailService;
import com.alexsaraiva.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevProfileConfigs {
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String recriarOuNaoDB;
	
	@Bean
	public boolean instantiateH2Database() throws ParseException {
		
		//Se no application-dev.properties não tiver "create" ele nao vai recriar o db 
		if(!"create".equals(recriarOuNaoDB)) {
			return false;
		}
		dbService.InstantiateDataBase();
		return true;
	}

	
	//Toda vez que instanciar um emailservice esse bean retorna o  pois é teste. 
		@Bean
		public EmailService emailService() {
			return new SmtpEmailService();
		}
	
}
