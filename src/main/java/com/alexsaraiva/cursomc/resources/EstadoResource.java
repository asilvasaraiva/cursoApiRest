package com.alexsaraiva.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alexsaraiva.cursomc.domain.Estado;
import com.alexsaraiva.cursomc.dto.CidadeDTO;
import com.alexsaraiva.cursomc.dto.EstadoDTO;
import com.alexsaraiva.cursomc.services.CidadeService;
import com.alexsaraiva.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Estado> findEstado(@PathVariable Integer id){
		Estado est = estadoService.findEstado(id);
		return ResponseEntity.ok().body(est);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> listaEstados(){
		return ResponseEntity.ok().body(estadoService.listEstadosDTO());
	}
	

	@RequestMapping(value = "/{id_estado}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> getCidade(@PathVariable(name = "id_estado") Integer idEstado){
		
		return ResponseEntity.ok().body(cidadeService.listCidadesDTO(idEstado));
	}
}
