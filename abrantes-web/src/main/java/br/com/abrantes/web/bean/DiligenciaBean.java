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

import br.com.abrantes.cmn.entity.Diligencia;
import br.com.abrantes.cmn.entity.OrigemProcesso;
import br.com.abrantes.cmn.entity.TpContratacao;
import br.com.abrantes.cmn.entity.TpDiligencia;
import br.com.abrantes.cmn.service.DiligenciaService;
import br.com.abrantes.cmn.service.OrigemProcessoService;
import br.com.abrantes.cmn.service.TpContratacaoService;
import br.com.abrantes.cmn.service.TpDiligenciaService;
import br.com.abrantes.cmn.util.jsf.AbstractBean;
import br.com.abrantes.cmn.util.jsf.JSFUtil;
import br.com.abrantes.cmn.util.jsf.Variaveis;
import br.com.abrantes.cmn.util.validators.ValidaPermissao;
import br.com.abrantes.ngc.functions.MenuControl;

@RequestScoped
@ManagedBean
public class DiligenciaBean extends AbstractBean<Diligencia, DiligenciaService>
{
	private JSFUtil util = new JSFUtil();
	
	private List<TpDiligencia> listaTpDiligencia;
	
	private List<OrigemProcesso> listaOrigemProcesso;
	
	private List<TpContratacao> listaTpContratacao;
	
	public DiligenciaBean()
	{
		super(DiligenciaService.getInstancia());
		this.ACTION_SEARCH = "diligencia";
		this.pageTitle = "Cadastro / Diligência";
		
		MenuControl.ativarMenu("flgMenuCad");
		MenuControl.ativarSubMenu("flgMenuCadDil");
	}
	
	@Override
	protected void setListaInserir() throws Exception
	{
		adicionarListaTpDiligencia();
		adicionarListaTpContratacao();
		adicionarListaOrigemProcesso();
	}

	private void adicionarListaTpDiligencia() throws Exception {
		List<TpDiligencia> resultTpDiligencia = TpDiligenciaService.getInstancia().pesquisar(new TpDiligencia(), 0);
		
		TpDiligencia tpDiligencia = new TpDiligencia();
		tpDiligencia.setDesTpDiligencia("Escolha o Tipo de Diligência");
		
		List<TpDiligencia> listaTpDiligencia = new ArrayList<TpDiligencia>();
		listaTpDiligencia.add(tpDiligencia);
		listaTpDiligencia.addAll(resultTpDiligencia);
		
		this.setListaTpDiligencia(listaTpDiligencia);
	}
	
	private void adicionarListaTpContratacao() throws Exception {
		List<TpContratacao> resultTpContratacao = TpContratacaoService.getInstancia().pesquisar(new TpContratacao(), 0);
		
		TpContratacao tpContratacao = new TpContratacao();
		tpContratacao.setDesTpContratacao("Escolha o Tipo de Contratação");
		
		List<TpContratacao> listaTpContratacao = new ArrayList<TpContratacao>();
		listaTpContratacao.add(tpContratacao);
		listaTpContratacao.addAll(resultTpContratacao);
		
		this.setListaTpContratacao(listaTpContratacao);
	}
	
	private void adicionarListaOrigemProcesso() throws Exception {
		List<OrigemProcesso> resultOrigemProcesso = OrigemProcessoService.getInstancia().pesquisar(new OrigemProcesso(), 0);
		
		OrigemProcesso origemProcesso = new OrigemProcesso();
		origemProcesso.setDesOrigemProcesso("Escolha a Origem Processo");
		
		List<OrigemProcesso> listaOrigemProcesso = new ArrayList<OrigemProcesso>();
		listaOrigemProcesso.add(origemProcesso);
		listaOrigemProcesso.addAll(resultOrigemProcesso);
		
		this.setListaOrigemProcesso(listaOrigemProcesso);
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
		return DiligenciaService.JOIN_USUARIO_CAD
			 | DiligenciaService.JOIN_USUARIO_ALT;				
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		this.getSearchObject().setFlgAtivo("T");
	}
	
	@Override
	protected void validarInserir() throws Exception
	{

	}
	
	public void inativar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_INATIVAR))
				{
					DiligenciaService.getInstancia().inativar(this.getEntity());
					
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
	public void preparaAlterar() 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
			{
				super.preparaAlterar();
				Diligencia diligencia = new Diligencia();
				diligencia.setIdDiligencia(getEntity().getIdDiligencia());
				
				diligencia = DiligenciaService.getInstancia().get(diligencia,DiligenciaService.JOIN_USUARIO_CAD
						   										   	  | DiligenciaService.JOIN_USUARIO_ALT);
				
				setEntity(diligencia);
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
					DiligenciaService.getInstancia().ativar(this.getEntity());
					
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
			Diligencia diligencia = new Diligencia();
			diligencia.setIdDiligencia(this.getEntity().getIdDiligencia());
						
			diligencia = DiligenciaService.getInstancia().get(diligencia, DiligenciaService.JOIN_USUARIO_CAD
															   		 | DiligenciaService.JOIN_USUARIO_ALT);
			
			this.setEntity(diligencia);
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
	}
	
	@Override
	protected boolean validarAcesso(String acao)
	{
		boolean temAcesso = true;

		if (!ValidaPermissao.getInstancia().verificaPermissao("usuario", acao))
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

	public List<TpDiligencia> getListaTpDiligencia() {
		return listaTpDiligencia;
	}

	public void setListaTpDiligencia(List<TpDiligencia> listaTpDiligencia) {
		this.listaTpDiligencia = listaTpDiligencia;
	}

	public List<OrigemProcesso> getListaOrigemProcesso() {
		return listaOrigemProcesso;
	}

	public void setListaOrigemProcesso(List<OrigemProcesso> listaOrigemProcesso) {
		this.listaOrigemProcesso = listaOrigemProcesso;
	}

	public List<TpContratacao> getListaTpContratacao() {
		return listaTpContratacao;
	}

	public void setListaTpContratacao(List<TpContratacao> listaTpContratacao) {
		this.listaTpContratacao = listaTpContratacao;
	}
}