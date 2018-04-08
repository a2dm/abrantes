package br.com.abrantes.web.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import br.com.abrantes.cmn.entity.ArquivoDiligencia;
import br.com.abrantes.cmn.entity.Diligencia;
import br.com.abrantes.cmn.entity.OrigemProcesso;
import br.com.abrantes.cmn.entity.Parametro;
import br.com.abrantes.cmn.entity.TpContratacao;
import br.com.abrantes.cmn.entity.TpDiligencia;
import br.com.abrantes.cmn.service.DiligenciaService;
import br.com.abrantes.cmn.service.OrigemProcessoService;
import br.com.abrantes.cmn.service.ParametroService;
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
	
	private String activeTab;
	
	private List<ArquivoDiligencia> listaArquivoDiligencia;
	
	private Part file;
	
	private String nomeArquivo;
	
	private ArquivoDiligencia arquivoDiligencia;
	
	public DiligenciaBean()
	{
		super(DiligenciaService.getInstancia());
		this.ACTION_SEARCH = "diligencia";
		this.pageTitle = "Cadastro / Diligência";
		
		MenuControl.ativarMenu("flgMenuCad");
		MenuControl.ativarSubMenu("flgMenuCadDil");
	}
	
	public String downloadArquivo()
	{
		try
		{
			Part file = this.getArquivoDiligencia().getFile();
				
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			        
			response.reset(); 
			response.setContentType("application/octet-stream");
			response.setContentLength(Integer.valueOf(this.getArquivoDiligencia().getTamanho()+""));
			response.setHeader("Content-disposition", "attachment; filename=" + this.getArquivoDiligencia().getNome());
			        
			OutputStream output = response.getOutputStream();		     
			
			if(this.getArquivoDiligencia().getIdArquivoDiligencia() == null)
			{
				output.write(IOUtils.toByteArray(file.getInputStream()));
			}
			else
			{
				Parametro parametro = new Parametro();
				parametro.setDescricao("FILES_DILIGENCIA");
				parametro = ParametroService.getInstancia().get(parametro, 0);
				
				FileInputStream is = new FileInputStream(parametro.getValor() + File.separator + this.getArquivoDiligencia().getIdDiligencia() + File.separator + this.getArquivoDiligencia().getNome());
			    output.write(IOUtils.toByteArray(is));
			}
			     
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
	protected void setListaInserir() throws Exception
	{
		this.setListaArquivoDiligencia(new ArrayList<ArquivoDiligencia>());
		
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
	
	public void completarAlterarAnexo() throws Exception 
	{
		this.getEntity().setDatAlteracao(new Date());
		this.getEntity().setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setListArquivo(this.getListaArquivoDiligencia());
	}
	
	public String alterarAnexo() 
	{
		try
	      {
	    	  if (validarAcesso(Variaveis.ACAO_ALTERAR))
	    	  {
	    		  completarAlterarAnexo();
	    		  setEntity(DiligenciaService.getInstancia().alterarAnexo(getEntity()));
	    		  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Documentação anexada com sucesso.", null));
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
		return cancelarDiligencia();
	}
	
	public String preparaAlterarDocumentacao() 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
			{
				super.preparaAlterar();
				Diligencia diligencia = new Diligencia();
				diligencia.setIdDiligencia(getEntity().getIdDiligencia());
				diligencia.setFiltroMap(new HashMap<>());
				diligencia.getFiltroMap().put("flgArquivoAtivo", "S");
				
				diligencia = DiligenciaService.getInstancia().get(diligencia,DiligenciaService.JOIN_USUARIO_CAD
						   										   	    | DiligenciaService.JOIN_USUARIO_ALT
						   										        | DiligenciaService.JOIN_ARQUIVO);
				
				if (diligencia != null) {
					if (diligencia.getListArquivo() != null && diligencia.getListArquivo().size() > 0) {
						
						this.setListaArquivoDiligencia(new ArrayList<>());
						
						for (ArquivoDiligencia element : diligencia.getListArquivo()) {
							if (this.fileExists(element)) {
								this.getListaArquivoDiligencia().add(element);
							}
						}
					}
				}
			}
		}
	    catch (Exception e)
	    {
	       FacesMessage message = new FacesMessage(e.getMessage());
	       message.setSeverity(FacesMessage.SEVERITY_ERROR);
	       FacesContext.getCurrentInstance().addMessage(null, message);
	    }
		
		return "diligenciaDocumentacao";
	}
	
	private boolean fileExists(ArquivoDiligencia element) throws Exception {
		
		Parametro parametro = new Parametro();
		parametro.setDescricao("FILES_DILIGENCIA");
		parametro = ParametroService.getInstancia().get(parametro, 0);
		
		File folder = new File(parametro.getValor() + File.separator + element.getIdDiligencia());
		String nomeArquivoSaida = folder.getPath() + File.separator + element.getNome();
		
		return new File(nomeArquivoSaida).exists();
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
            
            ArquivoDiligencia arquivoDiligencia = new ArquivoDiligencia();
            arquivoDiligencia.setIdDiligencia(getEntity().getIdDiligencia());
            arquivoDiligencia.setNome(this.getFileName(file));
            arquivoDiligencia.setDesArquivo(this.getFileName(file));
            arquivoDiligencia.setTipo(file.getContentType());
            arquivoDiligencia.setTamanho(file.getSize());
            arquivoDiligencia.setFile(file);
            arquivoDiligencia.setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
            arquivoDiligencia.setDatCadastro(new Date());
            arquivoDiligencia.setFlgAtivo("S");
            
            if(this.getListaArquivoDiligencia() != null
            		&& this.getListaArquivoDiligencia().size() > 0)
            {
            	for (ArquivoDiligencia obj : this.getListaArquivoDiligencia())
            	{
					if(obj.getNome().trim().equals(arquivoDiligencia.getNome().trim()))
					{
						throw new Exception("Este arquivo já foi adicionado na lista.");
					}
				}
            	
            	if(this.getListaArquivoDiligencia().size() >= 5)
            	{
            		throw new Exception("Só é permitido anexar 5 arquivos por audiência.");
            	}
            } else {
            	this.setListaArquivoDiligencia(new ArrayList<>());
            }
            
            this.getListaArquivoDiligencia().add(arquivoDiligencia);
            DiligenciaService.getInstancia().salvarFileDiretorio(arquivoDiligencia);
        } 
		catch (Exception e)
		{
        	FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
        }
		
		return null;
	}
	
	public void excluirArquivo() throws Exception
	{
		ArquivoDiligencia arquivoDiligencia = new ArquivoDiligencia();
		
		for (int i = 0; i < this.getListaArquivoDiligencia().size(); i++)
		{
			if (this.getListaArquivoDiligencia().get(i).getNome().equals(this.getNomeArquivo())) {
				arquivoDiligencia = this.getListaArquivoDiligencia().get(i);
				this.getListaArquivoDiligencia().remove(i);
			}
		}
		
		DiligenciaService.getInstancia().excluirFileDiretorio(arquivoDiligencia);
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
	
	public String cancelarDiligencia()
	{
		this.preparaPesquisar();
		return "diligencia";
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

	public JSFUtil getUtil() {
		return util;
	}

	public void setUtil(JSFUtil util) {
		this.util = util;
	}

	public List<ArquivoDiligencia> getListaArquivoDiligencia() {
		return listaArquivoDiligencia;
	}

	public void setListaArquivoDiligencia(List<ArquivoDiligencia> listaArquivoDiligencia) {
		this.listaArquivoDiligencia = listaArquivoDiligencia;
	}

	public ArquivoDiligencia getArquivoDiligencia() {
		return arquivoDiligencia;
	}

	public void setArquivoDiligencia(ArquivoDiligencia arquivoDiligencia) {
		this.arquivoDiligencia = arquivoDiligencia;
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

}