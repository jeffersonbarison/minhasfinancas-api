package br.com.barison.minhasfinancas.service;

import java.util.Optional;

import br.com.barison.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> obterUsuarioPorId(Long id);

}
