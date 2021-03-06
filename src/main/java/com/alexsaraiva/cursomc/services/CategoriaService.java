package com.alexsaraiva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.Categoria;
import com.alexsaraiva.cursomc.dto.CategoriaDTO;
import com.alexsaraiva.cursomc.repositories.CategoriaRepository;
import com.alexsaraiva.cursomc.services.exception.DataIntegrityException;
import com.alexsaraiva.cursomc.services.exception.ObjectNotFoundException;



@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = categoriaRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: "+id+" não disponivel. Tipo:"+ Categoria.class.getName()
				));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return categoriaRepository.save(obj);
	}
	
	public Categoria update (Categoria obj) {
		Categoria objNew = find(obj.getId());
		dataUpdate(objNew, obj);
		return categoriaRepository.save(obj);
	}
	
	private void dataUpdate(Categoria objNew, Categoria obj) {
		objNew.setNome(obj.getNome()); 
	}
	
	public void delete (Integer id) {
		find(id);
		try {
			categoriaRepository.deleteById(id);
		}catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não foi possível excluir a categoria "
					+ "pois existem produtos associados");
		}
		
	}
	
	public List<Categoria> findAll(){
		return categoriaRepository.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer LinesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, LinesPerPage, Direction.valueOf(direction), orderBy);
		return categoriaRepository.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
}
