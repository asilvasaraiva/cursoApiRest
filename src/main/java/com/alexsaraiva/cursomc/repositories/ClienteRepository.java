package com.alexsaraiva.cursomc.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.alexsaraiva.cursomc.domain.Cliente;


import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

	@Transactional(readOnly = true)
	Cliente findByEmail(String email);
}
