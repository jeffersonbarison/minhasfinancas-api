package br.com.barison.minhasfinancas.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.barison.minhasfinancas.api.dto.UsuarioDTO;
import br.com.barison.minhasfinancas.exception.ErroAutenticacao;
import br.com.barison.minhasfinancas.exception.RegraNegocioException;
import br.com.barison.minhasfinancas.model.entity.Usuario;
import br.com.barison.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	
	private UsuarioService service;
	
	public UsuarioController(UsuarioService service) {
		this.service = service;
	}
	
	
	@PostMapping("/autenticar")
	public ResponseEntity<Object> autenticar(@RequestBody UsuarioDTO dto){
		
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(),dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch(ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping
	public ResponseEntity <Object>salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder()
				                 .nome(dto.getNome())
				                 .senha(dto.getSenha())
				                 .email(dto.getEmail()).build();
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity<Object>(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());			
		}
		
	}
}
