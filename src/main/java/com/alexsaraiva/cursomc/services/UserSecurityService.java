package com.alexsaraiva.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.repositories.ClienteRepository;
import com.alexsaraiva.cursomc.security.UserSecurity;


@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private ClienteRepository repository;
	
	//Verifica se o usuário informado existe e é valido
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cli =  repository.findByEmail(email);
		if(cli==null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSecurity(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}

}
