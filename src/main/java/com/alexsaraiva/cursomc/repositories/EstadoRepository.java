package com.alexsaraiva.cursomc.repositories;

import org.springframework.stereotype.Repository;

import com.alexsaraiva.cursomc.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer>{

}
