package com.alexsaraiva.cursomc.repositories;

import org.springframework.stereotype.Repository;

import com.alexsaraiva.cursomc.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{

}
