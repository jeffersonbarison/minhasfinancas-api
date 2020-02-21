package br.com.barison.minhasfinancas.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.barison.minhasfinancas.api.dto.LancamentoDTO;
import br.com.barison.minhasfinancas.exception.ErroAutenticacao;
import br.com.barison.minhasfinancas.exception.RegraNegocioException;
import br.com.barison.minhasfinancas.model.entity.Lancamento;
import br.com.barison.minhasfinancas.model.entity.Usuario;
import br.com.barison.minhasfinancas.model.enums.StatusLancamento;
import br.com.barison.minhasfinancas.model.enums.TipoLancamento;
import br.com.barison.minhasfinancas.service.LancamentoService;
import br.com.barison.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController{
	
	@Autowired
	private LancamentoService service;
    
	@Autowired
	private UsuarioService serviceUsuario;
	
	
	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody LancamentoDTO dto ){		
		
		try {
			Lancamento lancamento = conveter(dto);
			lancamento = service.salvar(lancamento);
			return new ResponseEntity<Object>(lancamento,HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	}
	
	
	@PutMapping("{id}")
	public ResponseEntity<Object> atualizar(@PathVariable("id") Long id,  @RequestBody LancamentoDTO dto ){		
		
		return service.obterLancamentoPorId(id).map(entity -> { 
			try {
				Lancamento lancamento = conveter(dto);
				lancamento.setId(entity.getId());				
				return new ResponseEntity<Object>(lancamento,HttpStatus.OK);
			}catch(RegraNegocioException e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			
		}).orElseGet( () -> new ResponseEntity<Object>("Lançamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST));
			
			
	}
	
	private Lancamento conveter(LancamentoDTO dto) {
		Usuario usuario= serviceUsuario
				                   .obterUsuarioPorId(dto.getUsuario())
				                   .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado.") );
				                   
		Lancamento lancamento = Lancamento.builder()
				.id(dto.getId())
                .ano(dto.getAno())
                .mes(dto.getMes())
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .usuario(usuario)
                .tipo(TipoLancamento.valueOf(dto.getTipo()))
                .status(StatusLancamento.valueOf(dto.getStatus()))
                .build();
		return lancamento;
	}
}
