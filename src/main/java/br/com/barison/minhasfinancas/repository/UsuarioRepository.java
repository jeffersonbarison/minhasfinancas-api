package br.com.barison.minhasfinancas.repository;

import java.util.Optional;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.barison.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

	Optional<Usuario> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
}
