package br.com.barison.minhasfinancas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.barison.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	
}
