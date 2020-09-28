package com.alexsaraiva.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.alexsaraiva.cursomc.security.UserSecurity;



public class UserService {

	//Retorna o usuário logado no sistema e suas permissoes(authorities)
	public static UserSecurity  authenticated() {
		try {
			return (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
		}catch(Exception e) {
			return null;
		}
	}
}
