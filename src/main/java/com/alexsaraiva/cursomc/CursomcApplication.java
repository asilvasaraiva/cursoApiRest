package com.alexsaraiva.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alexsaraiva.cursomc.domain.Categoria;
import com.alexsaraiva.cursomc.domain.Cidade;
import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.domain.Endereco;
import com.alexsaraiva.cursomc.domain.Estado;
import com.alexsaraiva.cursomc.domain.Pagamento;
import com.alexsaraiva.cursomc.domain.PagamentoComBoleto;
import com.alexsaraiva.cursomc.domain.PagamentoComCartao;
import com.alexsaraiva.cursomc.domain.Pedido;
import com.alexsaraiva.cursomc.domain.Produto;
import com.alexsaraiva.cursomc.domain.enums.EstadoPagamento;
import com.alexsaraiva.cursomc.domain.enums.TipoCliente;
import com.alexsaraiva.cursomc.repositories.CategoriaRepository;
import com.alexsaraiva.cursomc.repositories.CidadeRepository;
import com.alexsaraiva.cursomc.repositories.ClienteRepository;
import com.alexsaraiva.cursomc.repositories.EnderecoRepository;
import com.alexsaraiva.cursomc.repositories.EstadoRepository;
import com.alexsaraiva.cursomc.repositories.PagamentoRepository;
import com.alexsaraiva.cursomc.repositories.PedidoRepository;
import com.alexsaraiva.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	 CategoriaRepository categoriaRepository;
	
	@Autowired
	 ProdutoRepository produtoRepository;
	
	@Autowired
	 EstadoRepository estadoRepository;
	
	@Autowired
	 CidadeRepository cidadeRepository;
	
	@Autowired
	 ClienteRepository clienteRepository;
	
	@Autowired
	 EnderecoRepository enderecoRepository;
	
	@Autowired
	 PedidoRepository pedidoRepository;
	
	@Autowired
	 PagamentoRepository pagamentoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");
		
		Produto p1 = new Produto(null,"computador",2000.00);
		Produto p2 = new Produto(null,"impressora",800.00);
		Produto p3 = new Produto(null,"mouse",80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlandia", est1);
		Cidade c2 = new Cidade(null, "Sao paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est1.getCidades().addAll(Arrays.asList(c2,c3));
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Maria", "asdhasn@dadsa", "02983823", TipoCliente.PESSOA_FISICA);
		cli1.getTelefones().addAll(Arrays.asList("12312312312","092390232908"));
		Endereco e1 = new Endereco(null, "Ruasd ", "200", "cep", "complemento", "bairro", cli1,c1);
		Endereco e2 = new Endereco(null, "testes ", "500", "cep2312", "c312312omplemento", "bai31231rro", cli1,c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
	clienteRepository.saveAll(Arrays.asList(cli1));
	enderecoRepository.saveAll(Arrays.asList(e1,e2));
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	Pedido pd1 = new Pedido(null, sdf.parse("30/12/2019 10:50"), cli1, e1);
	Pedido pd2 = new Pedido(null, sdf.parse("22/02/2019 10:50"), cli1, e1);
	
	Pagamento pgmt1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, pd1, 6);
	pd1.setPagamento(pgmt1);
	
	Pagamento pgmt2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, pd2,null,sdf.parse("30/12/2019 00:00"));
	pd2.setPagamento(pgmt2 );
	
	cli1.getPedidos().addAll(Arrays.asList(pd1, pd2));
	
	pedidoRepository.saveAll(Arrays.asList(pd1, pd2));
	pagamentoRepository.saveAll(Arrays.asList(pgmt1, pgmt2));
	
	}

}
