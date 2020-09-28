package com.alexsaraiva.cursomc.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.Cidade;
import com.alexsaraiva.cursomc.domain.Estado;
import com.alexsaraiva.cursomc.dto.CidadeDTO;
import com.alexsaraiva.cursomc.dto.EstadoDTO;
import com.alexsaraiva.cursomc.repositories.CidadeRepository;
import com.alexsaraiva.cursomc.repositories.EstadoRepository;
import com.alexsaraiva.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;

	// @Autowired
	// private EstadoService estadoService;

	public List<Cidade> findCidadeByEstado(Integer id) {
		// Estado estado = estadoService.findEstado(id);
		List<Cidade> cidades = cidadeRepository.findCidades(id);
		if (cidades == null) {
			throw new ObjectNotFoundException("Cidade não encontrada");
		}
		return cidades;
	}

	public List<CidadeDTO> listCidadesDTO(Integer id) {
		List<Cidade> listaCidades = findCidadeByEstado(id);

		List<CidadeDTO> lista = listaCidades.stream().map(x -> new CidadeDTO(x)).collect(Collectors.toList());

		return lista;
	}
}
