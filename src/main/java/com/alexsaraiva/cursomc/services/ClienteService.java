package com.alexsaraiva.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alexsaraiva.cursomc.domain.Cidade;
import com.alexsaraiva.cursomc.domain.Cliente;
import com.alexsaraiva.cursomc.domain.Endereco;
import com.alexsaraiva.cursomc.domain.enums.PerfilUsuario;
import com.alexsaraiva.cursomc.domain.enums.TipoCliente;
import com.alexsaraiva.cursomc.dto.ClienteDTO;
import com.alexsaraiva.cursomc.dto.ClienteNewDTO;
import com.alexsaraiva.cursomc.repositories.ClienteRepository;
import com.alexsaraiva.cursomc.repositories.EnderecoRepository;
import com.alexsaraiva.cursomc.security.UserSecurity;
import com.alexsaraiva.cursomc.services.exception.AuthorizationException;
import com.alexsaraiva.cursomc.services.exception.DataIntegrityException;
import com.alexsaraiva.cursomc.services.exception.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired
	private  ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pswrd;
	
	@Autowired
	private AWSS3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${image.prefix.cliente.profile}")
	private String prefixClient;
	
	@Value("${image.profile.size}")
	private Integer sizeImage;
	
	
	public Cliente find(String email) {
		Cliente cli= clienteRepository.findByEmail(email);
		
		if(cli==null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		return cli;
	}
	public Cliente find(Integer id) {
		
		UserSecurity user = UserService.authenticated();
		
		if(user==null || !user.hasRole(PerfilUsuario.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = clienteRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: "+id+" não disponivel. Tipo:"+ Cliente.class.getName()
				));
	}
	
	@Transactional
	public Cliente  insert (Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return clienteRepository.save(obj);
	}
	
	
	public Cliente update (Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public void delete (Integer id) {
		find(id);
		try {
			clienteRepository.deleteById(id);
		}catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não foi possível excluir a categoria "
					+ "pois existem produtos associados");
		}
	}
	
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer LinesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, LinesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null,null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), 
				objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()),pswrd.encode(objDTO.getSenha()));
		Cidade cidade =new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), 
				objDTO.getCep(), objDTO.getComplemento(), objDTO.getBairro(), cli, cidade);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2()!=null) {
			cli.getTelefones().add(objDTO.getTelefone2()); 	
		}
		if(objDTO.getTelefone3()!=null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		return cli;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSecurity user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, sizeImage);
		String fileName = prefixClient+user.getId()+".jpg";
		
		
		return s3Service.updateFile(imageService.getInputStream(jpgImage, "jpg"),fileName,"image");
	}
	
}
