package br.com.barison.minhasfinancas.service;

import java.util.List;
import java.util.Optional;

import br.com.barison.minhasfinancas.model.entity.Lancamento;
import br.com.barison.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
	Optional<Lancamento> obterLancamentoPorId(Long id);

}
