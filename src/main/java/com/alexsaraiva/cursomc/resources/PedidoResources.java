package com.alexsaraiva.cursomc.resources;


import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alexsaraiva.cursomc.domain.Categoria;
import com.alexsaraiva.cursomc.domain.Pedido;
import com.alexsaraiva.cursomc.dto.CategoriaDTO;
import com.alexsaraiva.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResources {

	
	@Autowired
	private PedidoService pedidoService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> listar(@PathVariable Integer id) {
		
		Pedido categoria = pedidoService.find(id);
		return ResponseEntity.ok().body(categoria);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){

		Pedido objResponse = pedidoService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(objResponse.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(
			@RequestParam(value= "page", defaultValue = "0") Integer page, 
			@RequestParam(value= "linesPerPage", defaultValue = "24")Integer LinesPerPage, 
			@RequestParam(value= "orderBy", defaultValue = "instante")String orderBy, 
			@RequestParam(value= "direction", defaultValue = "DESC")String direction) {
		Page<Pedido> listaPedidos = pedidoService.findPage(page, LinesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(listaPedidos);
	}
}
