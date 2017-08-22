package br.com.abrantes.web.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.abrantes.cmn.entity.Cliente;
import br.com.abrantes.cmn.entity.Estado;
import br.com.abrantes.cmn.service.ClienteService;
import br.com.abrantes.cmn.service.EstadoService;
import br.com.abrantes.cmn.util.jsf.AbstractBean;
import br.com.abrantes.cmn.util.jsf.JSFUtil;
import br.com.abrantes.cmn.util.jsf.Variaveis;
import br.com.abrantes.cmn.util.validators.ValidaPermissao;
import br.com.abrantes.ngc.functions.MenuControl;

@RequestScoped
@ManagedBean
public class ClienteBean extends AbstractBean<Cliente, ClienteService>
{
	private JSFUtil util = new JSFUtil();
	
	private List<Estado> listaEstado;
	
	private String siglaEstado;
	
	public ClienteBean()
	{
		super(ClienteService.getInstancia());
		this.ACTION_SEARCH = "cliente";
		this.pageTitle = "Manutenção / Empresa";
		
		MenuControl.ativarMenu("flgMenuMan");
		MenuControl.ativarSubMenu("flgMenuManCli");
	}
	
	@Override
	protected void setListaInserir() throws Exception
	{
		//LISTA DE ESTADOS
		List<Estado> resultEst = EstadoService.getInstancia().pesquisar(new Estado(), 0);
		
		Estado est = new Estado();
		est.setDescricao("Escolha o Estado");
		
		List<Estado> listaEstado = new ArrayList<Estado>();
		listaEstado.add(est);
		listaEstado.addAll(resultEst);
		
		this.setListaEstado(listaEstado);
	}
	
	@Override
	protected void completarPesquisar() throws Exception
	{
		if(this.getSearchObject().getFlgAtivo() != null
				&& this.getSearchObject().getFlgAtivo().equals("T"))
		{
			this.getSearchObject().setFlgAtivo(null);
		}
	}
	
	@Override
	protected int getJoinPesquisar()
	{
		return ClienteService.JOIN_USUARIO_CAD
			 | ClienteService.JOIN_USUARIO_ALT;				
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		this.getSearchObject().setFlgAtivo("T");
	}
	
	@Override
	protected void validarInserir() throws Exception
	{
		if(this.getEntity() == null
				|| this.getEntity().getDesCliente() == null
				|| this.getEntity().getDesCliente().trim().equals(""))
		{
			throw new Exception("O campo Nome Fantasia é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getRazaoSocial() == null
				|| this.getEntity().getRazaoSocial().trim().equals(""))
		{
			throw new Exception("O campo Razão Social é obrigatório!");
		}
		
		if(this.getEntity().getCnpj() == null || this.getEntity().getCnpj().trim().equals(""))
		{
			throw new Exception("O campo CNPJ é obrigatório.");
		}
		
		if(this.getEntity().getTelefone() == null || this.getEntity().getTelefone().trim().equals(""))
		{
			throw new Exception("O campo Telefone é obrigatório.");
		}
		
		if(this.getEntity().getEmail() == null || this.getEntity().getEmail().trim().equals(""))
		{
			throw new Exception("O campo E-mail é obrigatório.");
		}
		
		if(this.getEntity().getCep() == null || this.getEntity().getCep().trim().equals(""))
		{
			throw new Exception("O campo CEP é obrigatório.");
		}
		
		if(this.getEntity().getLogradouro() == null || this.getEntity().getLogradouro().trim().equals(""))
		{
			throw new Exception("O campo Logradouro é obrigatório.");
		}
	
		if(this.getEntity().getNumEndereco() == null || this.getEntity().getNumEndereco().trim().equals(""))
		{
			throw new Exception("O campo Número é obrigatório.");
		}
		
		if(this.getEntity().getBairro() == null || this.getEntity().getBairro().trim().equals(""))
		{
			throw new Exception("O campo Bairro é obrigatório.");
		}
		
		if(this.getEntity().getCidade() == null || this.getEntity().getCidade().trim().equals(""))
		{
			throw new Exception("O campo Cidade é obrigatório.");
		}
		
		if(this.siglaEstado == null || this.siglaEstado.trim().equals(""))
		{
			throw new Exception("O campo Estado é obrigatório.");
		}
	}
	
	@Override
	public void preparaAlterar() 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
			{
				super.preparaAlterar();
				Cliente cliente = new Cliente();
				cliente.setIdCliente(getEntity().getIdCliente());
				
				cliente = ClienteService.getInstancia().get(cliente, ClienteService.JOIN_ESTADO
																   | ClienteService.JOIN_USUARIO_CAD
						   										   | ClienteService.JOIN_USUARIO_ALT);
				
				if (cliente.getIdEstado() != null
						&& cliente.getIdEstado().intValue() > 0)  
				{
					Estado estado = new Estado();
					estado.setIdEstado(cliente.getIdEstado());				
					estado = EstadoService.getInstancia().get(estado, 0);
					this.setSiglaEstado(estado.getSigla());
				}
				
				setEntity(cliente);
			}
		}
	    catch (Exception e)
	    {
	       FacesMessage message = new FacesMessage(e.getMessage());
	       message.setSeverity(FacesMessage.SEVERITY_ERROR);
	       FacesContext.getCurrentInstance().addMessage(null, message);
	    }
	}

	public void inativar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_INATIVAR))
				{
					ClienteService.getInstancia().inativar(this.getEntity());
					
					FacesMessage message = new FacesMessage("Registro inativado com sucesso!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			}
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}		
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void completarAlterar() throws Exception 
	{
		this.validarInserir();
		this.getEntity().setDatAlteracao(new Date());
		this.getEntity().setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
	}
	
	public void ativar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_ATIVAR))
				{
					ClienteService.getInstancia().ativar(this.getEntity());
					
					FacesMessage message = new FacesMessage("Registro ativado com sucesso!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			}
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}		
	}
	
	public void visualizar()
	{
		try
		{
			Cliente cliente = new Cliente();
			cliente.setIdCliente(this.getEntity().getIdCliente());
						
			cliente = ClienteService.getInstancia().get(cliente, ClienteService.JOIN_ESTADO 
					                                           | ClienteService.JOIN_USUARIO_CAD
															   | ClienteService.JOIN_USUARIO_ALT);
			
			this.setEntity(cliente);
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	@Override
	protected void completarInserir() throws Exception
	{
		this.getEntity().setFlgAtivo("S");
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		
		if (this.getSiglaEstado() != null && !this.getSiglaEstado().equalsIgnoreCase(""))  
		{
			Estado estado = new Estado();
			estado.setSigla(this.getSiglaEstado());
			estado = EstadoService.getInstancia().get(estado, 0);
			
			this.getEntity().setIdEstado(estado.getIdEstado());
		}
	}
	
	@Override
	protected boolean validarAcesso(String acao)
	{
		boolean temAcesso = true;

		if (!ValidaPermissao.getInstancia().verificaPermissao("cliente", acao))
		{
			temAcesso = false;
			HttpServletResponse rp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			try
			{
				rp.sendRedirect("/abrantes-web/pages/acessoNegado.jsf");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return temAcesso;
	}

	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public String getSiglaEstado() {
		return siglaEstado;
	}

	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}
}