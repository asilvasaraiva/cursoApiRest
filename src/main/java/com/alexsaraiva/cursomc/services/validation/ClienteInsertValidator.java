package com.alexsaraiva.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.domain.enums.TipoCliente;
import com.alexsaraiva.cursomc.dto.ClienteNewDTO;
import com.alexsaraiva.cursomc.repositories.ClienteRepository;
import com.alexsaraiva.cursomc.resources.exception.FieldMessage;
import com.alexsaraiva.cursomc.services.validation.utils.BR_validaCPFeCNPJ;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
// inclua os testes aqui, inserindo erros na lista
		
		
		if(objDto.getTipo().equals(TipoCliente.PESSOA_FISICA.getCod()) && !BR_validaCPFeCNPJ.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add((new FieldMessage("cpfOuCnpj", "CPF inválido")));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCod()) && !BR_validaCPFeCNPJ.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add((new FieldMessage("cpfOuCnpj", "CNPJ inválido")));
		}
		
		Cliente testeEmailDuplicado = clienteRepository.findByEmail(objDto.getEmail());
		
		if(testeEmailDuplicado !=null) {
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