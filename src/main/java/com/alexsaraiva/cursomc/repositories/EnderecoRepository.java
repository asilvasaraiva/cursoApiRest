package com.alexsaraiva.cursomc.repositories;

import org.springframework.stereotype.Repository;



import com.alexsaraiva.cursomc.domain.Endereco;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer>{

}
