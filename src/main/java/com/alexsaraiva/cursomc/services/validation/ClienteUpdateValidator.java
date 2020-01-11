package com.alexsaraiva.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.dto.ClienteDTO;
import com.alexsaraiva.cursomc.repositories.ClienteRepository;
import com.alexsaraiva.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private HttpServletRequest uriRequest;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
// inclua os testes aqui, inserindo erros na lista
		
		@SuppressWarnings("unchecked")
		Map <String, String> map = (Map<String,String>) uriRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		Cliente testeEmailDuplicado = clienteRepository.findByEmail(objDto.getEmail());
		
		if(testeEmailDuplicado !=null && !testeEmailDuplicado.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "email ja cadastrado"));
		}
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}