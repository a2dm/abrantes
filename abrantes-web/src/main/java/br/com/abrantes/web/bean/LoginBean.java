package br.com.abrantes.web.bean;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.abrantes.cmn.entity.Usuario;
import br.com.abrantes.cmn.service.RecuperarSenhaService;
import br.com.abrantes.cmn.service.UsuarioService;
import br.com.abrantes.cmn.util.criptografia.CriptoMD5;
import br.com.abrantes.cmn.util.jsf.AbstractBean;
import br.com.abrantes.cmn.util.jsf.JSFUtil;
import br.com.abrantes.ngc.functions.MenuControl;


@RequestScoped
@ManagedBean
public class LoginBean extends AbstractBean<Usuario, UsuarioService>
{
	private Usuario usuario;
	private String emailRecuperarSenha;
	private String mensagem;
	
	private static final String ACAO_SUCESSO = "principal";
	private static final String ACAO_LOGOUT  = "login";
	
	public LoginBean()
	{
		usuario = new Usuario(); 
	}

	public String login()
	{
		JSFUtil util = new JSFUtil();
		
		try
		{
			//VALIDANDO CAMPO LOGIN			
			if(this.usuario.getLogin() == null 
					|| this.usuario.getLogin().trim().equals(""))
			{
				//MENSAGEM DE VALIDACAO
				
				FacesMessage message = new FacesMessage("O campo Login é obrigatório!");
		        message.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
			
			//VALIDANDO CAMPO SENHA
			if(this.usuario.getSenha() == null 
					|| this.usuario.getSenha().trim().equals(""))
			{
				//MENSAGEM DE VALIDACAO
				
				FacesMessage message = new FacesMessage("O campo Senha é obrigatório!");
		        message.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}

			Usuario usuario = new Usuario();
			usuario.setLogin(this.usuario.getLogin().toUpperCase().trim());			
			usuario.setSenha(CriptoMD5.stringHexa(this.usuario.getSenha().toUpperCase()));
			usuario.setFlgAtivo("S");
			
			usuario = UsuarioService.getInstancia().get(usuario, 0);
			
			if(usuario != null)
			{
				if(usuario.getIdGrupo() == null)
				{
					FacesMessage message = new FacesMessage("Você não está vinculado a um Perfil de Usuário. Favor entrar em contato com a administração do sistema.");
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
			        FacesContext.getCurrentInstance().addMessage(null, message);
					return null;
				}				
				
				util.getSession().setAttribute("loginUsuario", usuario);
				util.getSession().setAttribute("dataLogin", new Date());
				MenuControl.ativarMenu("flgMenuDsh");
				return ACAO_SUCESSO;
			}
			else
			{
				FacesMessage message = new FacesMessage("Os dados inseridos não corresponde a um usuário do sistema. Verifique seu Login e Senha e tente novamente.");
		        message.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage("Ocorreu um erro inesperado, favor contactar o administrador do sistema!");
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}		
		
	}
	
	public void preparaRecuperarSenha(ActionEvent event)
	{
		this.setMensagem(null);
		this.setEmailRecuperarSenha(null);
	}
	
	public void recuperarSenha(ActionEvent event)
	{
		try
		{
			this.setMensagem(null);
			
			if(this.getEmailRecuperarSenha() == null
					|| this.getEmailRecuperarSenha().trim().equals(""))
			{
				throw new Exception("Informe seu E-mail para o sistema enviar o link de recuperação de senha!");
			}
			
			RecuperarSenhaService.getInstancia().gerarHash(this.getEmailRecuperarSenha());
			
			FacesMessage message = new FacesMessage("O link de recuperação de senha foi enviado para o seu e-mail!");
	        message.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
		catch (Exception e)
		{
			this.setMensagem(e.getMessage());
		}
	}
	
	public String logout()
	{
		JSFUtil util = new JSFUtil();
		util.getSession().invalidate();
		
		return ACAO_LOGOUT;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getEmailRecuperarSenha() {
		return emailRecuperarSenha;
	}

	public void setEmailRecuperarSenha(String emailRecuperarSenha) {
		this.emailRecuperarSenha = emailRecuperarSenha;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
