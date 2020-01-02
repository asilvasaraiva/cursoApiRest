package com.alexsaraiva.cursomc.resources;


import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alexsaraiva.cursomc.domain.Categoria;
import com.alexsaraiva.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResources {

	
	@Autowired
	private CategoriaService categoriaService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> listar(@PathVariable Integer id) {
		Categoria categoria = categoriaService.buscar(id);
		return ResponseEntity.ok().body(categoria);
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria obj){
		Categoria objResponse = categoriaService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(objResponse.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	
}
