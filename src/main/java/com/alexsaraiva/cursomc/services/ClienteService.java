package com.alexsaraiva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.dto.ClienteDTO;
import com.alexsaraiva.cursomc.repositories.ClienteRepository;
import com.alexsaraiva.cursomc.services.exception.DataIntegrityException;
import com.alexsaraiva.cursomc.services.exception.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired
	private  ClienteRepository clienteRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: "+id+" não disponivel. Tipo:"+ Cliente.class.getName()
				));
	}
	
	public Cliente update (Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public void delete (Integer id) {
		find(id);
		try {
			clienteRepository.deleteById(id);
		}catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não foi possível excluir a categoria "
					+ "pois existem produtos associados");
		}
		
	}
	
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer LinesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, LinesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
		
		
	}
	
}
