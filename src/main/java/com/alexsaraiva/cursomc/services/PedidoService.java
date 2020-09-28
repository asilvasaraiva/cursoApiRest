package com.alexsaraiva.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexsaraiva.cursomc.domain.Categoria;
import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.domain.ItemPedido;
import com.alexsaraiva.cursomc.domain.PagamentoComBoleto;
import com.alexsaraiva.cursomc.domain.Pedido;
import com.alexsaraiva.cursomc.domain.enums.EstadoPagamento;
import com.alexsaraiva.cursomc.repositories.ItemPedidoRepository;
import com.alexsaraiva.cursomc.repositories.PagamentoRepository;
import com.alexsaraiva.cursomc.repositories.PedidoRepository;
import com.alexsaraiva.cursomc.repositories.ProdutoRepository;
import com.alexsaraiva.cursomc.security.UserSecurity;
import com.alexsaraiva.cursomc.services.exception.AuthorizationException;
import com.alexsaraiva.cursomc.services.exception.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository ;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository ;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: "+id+" não disponivel. Tipo:"+ Pedido.class.getName()
				));
	}
	
	//Coloar o @Transactional no Insert
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgt = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgt, obj.getInstante());
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			//ip.setPreco(produtoRepository.findById(ip.getProduto().getId()).get().getPreco());//2 gets pq o repository retorna um Optional<Produto>.
			ip.setProduto(produtoRepository.findById(ip.getProduto().getId()).get());
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		/*EmailService é uma interface, o retorno dela está atrelada ao perfil utilizado
		 * definido no application.properties e na relativa classe de configuracoes 
		 * no pacote .. cursomc.config. 
		 * Logo se estiver no perfil de teste, o email retorna um MockEmail(email no console), 
		 * se estiver no perfil prod/dev envia um email de verdade.
		 */
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}
	
	
	public Page<Pedido> findPage(Integer page, Integer LinesPerPage, String orderBy, String direction){
		
		UserSecurity user = UserService.authenticated();
		if(user==null) {
			throw new AuthorizationException("Acesso Negado");
		}
		PageRequest pageRequest = PageRequest.of(page, LinesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return pedidoRepository.findByCliente(cliente, pageRequest);
		
	}
	
}
