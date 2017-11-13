package br.com.abrantes.web.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import br.com.abrantes.cmn.entity.ArquivoAudiencia;
import br.com.abrantes.cmn.entity.Audiencia;
import br.com.abrantes.cmn.entity.OrigemProcesso;
import br.com.abrantes.cmn.entity.TpAudiencia;
import br.com.abrantes.cmn.entity.TpContratacao;
import br.com.abrantes.cmn.service.AudienciaService;
import br.com.abrantes.cmn.service.OrigemProcessoService;
import br.com.abrantes.cmn.service.TpAudienciaService;
import br.com.abrantes.cmn.service.TpContratacaoService;
import br.com.abrantes.cmn.util.jsf.AbstractBean;
import br.com.abrantes.cmn.util.jsf.JSFUtil;
import br.com.abrantes.cmn.util.jsf.Variaveis;
import br.com.abrantes.cmn.util.validators.ValidaPermissao;
import br.com.abrantes.ngc.functions.MenuControl;

@RequestScoped
@ManagedBean
public class AudienciaBean extends AbstractBean<Audiencia, AudienciaService>
{
	private JSFUtil util = new JSFUtil();
	
	private List<TpAudiencia> listaTpAudiencia;
	
	private List<OrigemProcesso> listaOrigemProcesso;
	
	private List<TpContratacao> listaTpContratacao;
	
	private String activeTab;
	
	private List<ArquivoAudiencia> listaArquivoAudiencia;
	 
	private Part file;
	
	private String nomeArquivo;
	
	private ArquivoAudiencia arquivoAudiencia;
	
	
	public AudienciaBean()
	{
		super(AudienciaService.getInstancia());
		this.ACTION_SEARCH = "audiencia";
		this.pageTitle = "Cadastro / Audiência";
		
		MenuControl.ativarMenu("flgMenuCad");
		MenuControl.ativarSubMenu("flgMenuCadAud");
		
		this.setActiveTab("Documentação");
	}
	
	public String preparaCadastrarAudiencia()
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_INSERIR))
			{
				setEntity(getNewEntityInstance());
	    		setCurrentState(STATE_INSERT);
	    		 
	    		setDefaultInserir();
	    		setListaInserir();
			}
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			if(e.getMessage() == null)
				FacesContext.getCurrentInstance().addMessage("", message);
			else
				FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return "audienciaCadastrar";
   }

	@Override
	protected void setListaInserir() throws Exception
	{
		this.setListaArquivoAudiencia(new ArrayList<ArquivoAudiencia>());
		
		adicionarListaTpAudiencia();
		adicionarListaTpContratacao();
		adicionarListaOrigemProcesso();
	}

	private void adicionarListaTpAudiencia() throws Exception {
		List<TpAudiencia> resultTpAudiencia = TpAudienciaService.getInstancia().pesquisar(new TpAudiencia(), 0);
		
		TpAudiencia tpAudiencia = new TpAudiencia();
		tpAudiencia.setDesTpAudiencia("Escolha o Tipo de Audiência");
		
		List<TpAudiencia> listaTpAudiencia = new ArrayList<TpAudiencia>();
		listaTpAudiencia.add(tpAudiencia);
		listaTpAudiencia.addAll(resultTpAudiencia);
		
		this.setListaTpAudiencia(listaTpAudiencia);
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
		return AudienciaService.JOIN_USUARIO_CAD
			 | AudienciaService.JOIN_USUARIO_ALT;				
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
	
	public void inserirAudiencia(ActionEvent event) 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_INSERIR))
			{
				validarInserir();
				completarInserir();
				setEntity(AudienciaService.getInstancia().inserir(getEntity()));
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro inserido com sucesso.", null));
				this.preparaCadastrarAudiencia();
			}
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			if(e.getMessage() == null)
				FacesContext.getCurrentInstance().addMessage("", message);
			else
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
					AudienciaService.getInstancia().inativar(this.getEntity());
					
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
				Audiencia audiencia = new Audiencia();
				audiencia.setIdAudiencia(getEntity().getIdAudiencia());
				
				audiencia = AudienciaService.getInstancia().get(audiencia,AudienciaService.JOIN_USUARIO_CAD
						   										   	    | AudienciaService.JOIN_USUARIO_ALT
						   										        | AudienciaService.JOIN_ARQUIVO);
				
				setEntity(audiencia);
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
					AudienciaService.getInstancia().ativar(this.getEntity());
					
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
			Audiencia audiencia = new Audiencia();
			audiencia.setIdAudiencia(this.getEntity().getIdAudiencia());
						
			audiencia = AudienciaService.getInstancia().get(audiencia, AudienciaService.JOIN_USUARIO_CAD
															   		 | AudienciaService.JOIN_USUARIO_ALT);
			
			this.setEntity(audiencia);
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public String downloadArquivo()
	{
		try
		{
			Part file = this.getArquivoAudiencia().getFile();
			
			 FacesContext facesContext = FacesContext.getCurrentInstance();
			 HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		        
			 response.reset(); 
		     response.setContentType("application/octet-stream");
		     response.setContentLength(Integer.valueOf(file.getSize()+""));
		     response.setHeader("Content-disposition", "attachment; filename=" + this.getArquivoAudiencia().getNome());
		        
		     OutputStream output = response.getOutputStream();		     
		     output.write(IOUtils.toByteArray(file.getInputStream()));
		     
		     output.close();
		     facesContext.responseComplete(); 
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return null;
	}
	
	@Override
	protected void completarInserir() throws Exception
	{
		this.getEntity().setFlgAtivo("S");
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setListArquivo(this.getListaArquivoAudiencia());
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

	public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		if (file.getSize() > 1024) {
			msgs.add(new FacesMessage("file too big"));
		}
		if (!"text/plain".equals(file.getContentType())) {
			msgs.add(new FacesMessage("not a text file"));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}

	@SuppressWarnings("resource")
	public String importar()
	{
		try
		{			
			new Scanner(file.getInputStream()).useDelimiter("\\A").next();
			
			this.validar(file);
            
            ArquivoAudiencia arquivoAudiencia = new ArquivoAudiencia();
            arquivoAudiencia.setNome(this.getFileName(file));
            arquivoAudiencia.setTipo(file.getContentType());
            arquivoAudiencia.setTamanho(file.getSize());
            arquivoAudiencia.setFile(file);
            
            if(this.getListaArquivoAudiencia() != null
            		&& this.getListaArquivoAudiencia().size() > 0)
            {
            	for (ArquivoAudiencia obj : this.getListaArquivoAudiencia())
            	{
					if(obj.getNome().trim().equals(arquivoAudiencia.getNome().trim()))
					{
						throw new Exception("Este arquivo já foi adicionado na lista.");
					}
				}
            }
            	
            if(this.getListaArquivoAudiencia().size() >= 5)
            {
            	throw new Exception("Só é permitido anexar 5 arquivos por audiência.");
            }
            
            this.getListaArquivoAudiencia().add(arquivoAudiencia);
            
            this.setActiveTab("Documentação");            
        } 
		catch (Exception e)
		{
        	FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
        }
		
		return null;
	}
	
	public void excluirArquivo()
	{
		for (int i = 0; i < this.getListaArquivoAudiencia().size(); i++)
		{
			if(this.getListaArquivoAudiencia().get(i).getNome().equals(this.getNomeArquivo()))
			this.getListaArquivoAudiencia().remove(i);
		}
		
	}
	
	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}
	
	public void validar(Part value) throws Exception
	{
        Part arquivo = (Part) value;

        if (arquivo.getSize() > (20*1024*1024)) {
            throw new Exception("Arquivo muito grande. O arquivo deve ter o tamanho máximo de 20mb.");
        }

        if (!"application/pdf".equals(arquivo.getContentType())
        		&& !"application/msword".equals(arquivo.getContentType())
        		&& !"image/jpg".equals(arquivo.getContentType())
        		&& !"image/jpeg".equals(arquivo.getContentType())
        		&& !"image/png".equals(arquivo.getContentType())) {
            throw new Exception("Tipo de arquivo inválido, O arquivo deve ser dos tipos: .PDF, .DOC, .JPG, .JPEG, .PNG.");
        }
    }
	
	public String cancelarAudiencia()
	{
		this.preparaPesquisar();
		return "audiencia";
	}
	
	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public List<TpAudiencia> getListaTpAudiencia() {
		return listaTpAudiencia;
	}

	public void setListaTpAudiencia(List<TpAudiencia> listaTpAudiencia) {
		this.listaTpAudiencia = listaTpAudiencia;
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

	public List<ArquivoAudiencia> getListaArquivoAudiencia() {
		return listaArquivoAudiencia;
	}

	public void setListaArquivoAudiencia(List<ArquivoAudiencia> listaArquivoAudiencia) {
		this.listaArquivoAudiencia = listaArquivoAudiencia;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public ArquivoAudiencia getArquivoAudiencia() {
		return arquivoAudiencia;
	}

	public void setArquivoAudiencia(ArquivoAudiencia arquivoAudiencia) {
		this.arquivoAudiencia = arquivoAudiencia;
	}
}