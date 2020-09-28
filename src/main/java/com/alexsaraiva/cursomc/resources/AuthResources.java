package com.alexsaraiva.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alexsaraiva.cursomc.dto.EmailDTO;
import com.alexsaraiva.cursomc.security.JWTUtils;
import com.alexsaraiva.cursomc.security.UserSecurity;
import com.alexsaraiva.cursomc.services.AuthService;
import com.alexsaraiva.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResources {
	
	@Autowired
	private JWTUtils jwtUtil;
	
	@Autowired
	private AuthService authService;
	
	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSecurity user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgotPassword(@Valid @RequestBody EmailDTO objDTO) {
		authService.sendNewPassword(objDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
}
