package br.com.barison.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.barison.minhasfinancas.exception.RegraNegocioException;
import br.com.barison.minhasfinancas.model.entity.Lancamento;
import br.com.barison.minhasfinancas.model.enums.StatusLancamento;
import br.com.barison.minhasfinancas.repository.LancamentoRepository;
import br.com.barison.minhasfinancas.service.LancamentoService;


@Service
public class LancamentoServiceImp implements LancamentoService{

	@Autowired
	private LancamentoRepository repository;
	
	/*public LancamentoServiceImp(LancamentoRepository repository) {
		super();
		this.repository = repository;
	}*/
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {		
		this.validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		this.validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example<Lancamento> example = Example.of(lancamentoFiltro, ExampleMatcher.matching()
				                                                     .withIgnoreCase()
				                                                     .withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		this.atualizar(lancamento);		
	}

	@Override
	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida.");
		}
		
		if (lancamento.getMes() == null ||lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		if (lancamento.getAno() == null ||lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if (lancamento.getUsuario() == null ||lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}
		
		if (lancamento.getValor() == null ||lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}
		
		if (lancamento.getTipo() == null ) {
			throw new RegraNegocioException("Informe um Tipo de Lançamento.");
		}
		
	}

	@Override
	public Optional<Lancamento> obterLancamentoPorId(Long id) {
		
		return repository.findById(id);
	}

}
