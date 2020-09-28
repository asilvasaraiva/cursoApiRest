package com.alexsaraiva.cursomc.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.Estado;
import com.alexsaraiva.cursomc.dto.EstadoDTO;
import com.alexsaraiva.cursomc.repositories.EstadoRepository;
import com.alexsaraiva.cursomc.services.exception.ObjectNotFoundException;



@Service
public class EstadoService {

	@Autowired
	private EstadoRepository estadoRepository;
	
	public Estado findEstado(Integer id) {
		 Optional<Estado> est = estadoRepository.findById(id);
			return est.orElseThrow(()->new ObjectNotFoundException("Estado não existente"));
	}
	
	public List<Estado> listEstados(){
		return estadoRepository.findAll();
	}
	
	public List<EstadoDTO> listEstadosDTO(){
		List<Estado> listaEstados = estadoRepository.findAllByOrderByNome();
		List<EstadoDTO> lista = listaEstados.stream().map(x-> new EstadoDTO(x)).collect(Collectors.toList());
		return lista;
	}
	
}
