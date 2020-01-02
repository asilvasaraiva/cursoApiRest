package com.alexsaraiva.cursomc.repositories;

import org.springframework.stereotype.Repository;

import com.alexsaraiva.cursomc.domain.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{

}
