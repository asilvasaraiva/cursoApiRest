package com.alexsaraiva.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.alexsaraiva.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	public void preencherPagamentoComBoleto(PagamentoComBoleto pgt, Date instantePedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instantePedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);//Adicionar 7 dias a data atual. 
		pgt.setDataVencimento(cal.getTime());//Setando o vencimento para 7 dias depois
	}
}
