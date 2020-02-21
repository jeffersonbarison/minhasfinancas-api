package br.com.barison.minhasfinancas.service;



import java.util.Optional;

import org.junit.Test;
import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import br.com.barison.minhasfinancas.exception.ErroAutenticacao;
import br.com.barison.minhasfinancas.exception.RegraNegocioException;
import br.com.barison.minhasfinancas.model.entity.Usuario;
import br.com.barison.minhasfinancas.repository.UsuarioRepository;
import br.com.barison.minhasfinancas.service.impl.UsuarioServiceImpl;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	private static final String emailUsuario = "usuario@email.com";
	private static final String senha = "12345";
	
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {
	   //cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = criarUsuario();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(usuario);		
		
		//verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
	}
	
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
	   //cenario
		Usuario usuario = criarUsuario();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(emailUsuario);
		
		//acao	
		service.salvarUsuario(usuario);
		
		//verificacao
		Mockito.verify(repository,Mockito.never()).save(usuario);
		
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		Usuario usuario = criarUsuario();
		Mockito.when(repository.findByEmail(emailUsuario)).thenReturn(Optional.of(usuario));		
		
		//acao
		Usuario result = service.autenticar(emailUsuario, senha);
		
		//Verificacao
		Assertions.assertThat(result).isNotNull();
		
	}
	
	@Test()
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenario		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());		
		
	  //acao		
	  Throwable exception =	Assertions.catchThrowable(() -> service.autenticar(emailUsuario, senha));	
		
	  //Verificacao
	  Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não cadastrado para o e-mail informado.");
		
		
	}
	
	
	@Test()
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenario
		Usuario usuario = criarUsuario();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));		
		
		//acao		
		Throwable exception =	Assertions.catchThrowable(() -> service.autenticar(emailUsuario, "123"));	
		
		//Verificacao
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
	}
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);	
		
		//acao
		service.validarEmail("usuario@email.com");	
		
	}
	
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		service.validarEmail("usuario@email.com");
	}
	
	public static Usuario criarUsuario() {
		return  Usuario.builder().id(1L).senha(senha).nome("nome").email(emailUsuario).build();
	}
}
