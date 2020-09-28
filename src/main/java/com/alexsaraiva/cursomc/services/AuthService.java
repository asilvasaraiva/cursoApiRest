package com.alexsaraiva.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.repositories.ClienteRepository;

@Service
public class AuthService {

	@Autowired
	private ClienteService clienteService; 
	
	@Autowired
	private EmailService emailService; 
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteService.find(email);
		
		String newPassword = newPassword();
		cliente.setSenha(passwordEncoder.encode(newPassword));
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente,newPassword);
		
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i = 0; i<10;i++) {
			vet[i]= randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if(opt==0) {//gera um digito (0-9) utilizando os valores da tabela Unicode. Digito começa a partir de 48
			return (char) (rand.nextInt(10)+48);
		}else if(opt==1) {//gera letra maiuscula. A maiusculo começa com valor 65 na tabela.
			return (char)(rand.nextInt(26)+65);
		}else {//gera letra minuscula. valores começam de 97
			return (char)(rand.nextInt(26)+97);
		}
	}
}
