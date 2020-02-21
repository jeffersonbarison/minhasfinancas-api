package br.com.barison.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.barison.minhasfinancas.exception.ErroAutenticacao;
import br.com.barison.minhasfinancas.exception.RegraNegocioException;
import br.com.barison.minhasfinancas.model.entity.Usuario;
import br.com.barison.minhasfinancas.repository.UsuarioRepository;
import br.com.barison.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;	
	
	
	/*public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;		
	}*/

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não cadastrado para o e-mail informado."); 
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		validarEmail(usuario.getEmail());
		return repository.save(usuario);		
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		
		if (existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail.");
		}
	}

}
