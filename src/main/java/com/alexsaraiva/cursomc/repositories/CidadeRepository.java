package com.alexsaraiva.cursomc.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.alexsaraiva.cursomc.domain.Cidade;
import com.alexsaraiva.cursomc.domain.Estado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{

	public List<Cidade> findByEstado(Estado estado);
	
	@Transactional(readOnly = true)
	@Query("SELECT obj FROM Cidade obj WHERE obj.estado.id = :estadoID ORDER BY obj.nome")
	public List<Cidade> findCidades(@Param("estadoID") Integer idEstado);
}
