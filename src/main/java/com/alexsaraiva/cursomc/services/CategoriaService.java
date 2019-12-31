package com.alexsaraiva.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.Categoria;
import com.alexsaraiva.cursomc.repositories.CategoriaRepository;
import com.alexsaraiva.cursomc.services.exception.ObjectNotFoundException;



@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = categoriaRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: "+id+" não disponivel. Tipo:"+ Categoria.class.getName()
				));
	}
}
