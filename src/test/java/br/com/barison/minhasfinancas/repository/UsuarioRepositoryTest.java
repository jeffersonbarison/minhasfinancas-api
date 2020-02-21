package br.com.barison.minhasfinancas.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.barison.minhasfinancas.model.entity.Usuario;

//@SpringBootTest
@RunWith(SpringRunner.class) 
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	private static final String emailUsuario = "usuario@email.com";
	
	@Test
	public void devVerificarAExistenciaDeUmEmail() {	
		//cenario
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);		
		
		//açaõ //execução
		boolean result = repository.existsByEmail(emailUsuario);
		
		//verificação
		Assertions.assertThat(result).isTrue();
   }
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//cenario
		
		
		//açaõ //execução
		boolean result = repository.existsByEmail(emailUsuario);
		
		//verificacao
		Assertions.assertThat(result).isFalse();		
	}
	
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenario
		Usuario usuario = criarUsuario();
	
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		//verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);	
		
		//Verirficacao
		Optional<Usuario> result = repository.findByEmail(emailUsuario);
		
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}
	
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioQuandoNaoExisteNaBase() {
				
		//Verirficacao
		Optional<Usuario> result = repository.findByEmail(emailUsuario);
		
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}
	
	public static Usuario criarUsuario() {
		return  Usuario.builder().nome("usuario").senha("senha").email(emailUsuario).build();
	}
	
}
