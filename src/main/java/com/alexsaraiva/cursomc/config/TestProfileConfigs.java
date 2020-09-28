package com.alexsaraiva.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.alexsaraiva.cursomc.services.DBService;
import com.alexsaraiva.cursomc.services.EmailService;
import com.alexsaraiva.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestProfileConfigs {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateH2Database() throws ParseException {
		dbService.InstantiateDataBase();
		return true;
	}
	
	
	//Toda vez que instanciar um emailservice esse bean retorna o Mock pois é teste. 
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
	

}
