package br.com.abrantes.web.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import br.com.abrantes.cmn.entity.ArquivoAudiencia;
import br.com.abrantes.cmn.entity.Audiencia;
import br.com.abrantes.cmn.entity.OrigemProcesso;
import br.com.abrantes.cmn.entity.TpAudiencia;
import br.com.abrantes.cmn.entity.TpContratacao;
import br.com.abrantes.cmn.service.ArquivoAudienciaService;
import br.com.abrantes.cmn.service.AudienciaService;
import br.com.abrantes.cmn.service.OrigemProcessoService;
import br.com.abrantes.cmn.service.TpAudienciaService;
import br.com.abrantes.cmn.service.TpContratacaoService;
import br.com.abrantes.cmn.util.jsf.AbstractBean;
import br.com.abrantes.cmn.util.jsf.JSFUtil;
import br.com.abrantes.cmn.util.jsf.Variaveis;
import br.com.abrantes.cmn.util.validators.ValidaPermissao;
import br.com.abrantes.cmn.vo.UploadedImage;
import br.com.abrantes.ngc.functions.MenuControl;

@RequestScoped
@ManagedBean
public class AudienciaBean extends AbstractBean<Audiencia, AudienciaService>
{
	private JSFUtil util = new JSFUtil();
	
	private List<TpAudiencia> listaTpAudiencia;
	
	private List<OrigemProcesso> listaOrigemProcesso;
	
	private List<TpContratacao> listaTpContratacao;
	
	private ArquivoAudiencia arquivo;
	
//	private UploadedImage file;
	
	private ArrayList<UploadedImage> files = new ArrayList<UploadedImage>();
	
	private Part file;
	
	private String fileContent;
	
	public AudienciaBean()
	{
		super(AudienciaService.getInstancia());
		this.ACTION_SEARCH = "audiencia";
		this.pageTitle = "Cadastro / Audiência";
		
		MenuControl.ativarMenu("flgMenuCad");
		MenuControl.ativarSubMenu("flgMenuCadAud");
	}
	
	public void adicionarArquivo() {
		try (InputStream input = file.getInputStream()) {
			UploadedImage uploadedImage = new UploadedImage();
			uploadedImage.setName(file.getName());
			uploadedImage.setLength(file.getSize());
			
			files.add(uploadedImage);
		}
	    catch (IOException e) {
	        // Show faces message?
	    }
	}
	
	@Override
	protected void setListaInserir() throws Exception
	{
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
	
	@Override
	protected void completarInserir() throws Exception
	{
		this.getEntity().setFlgAtivo("S");
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
	}
	
	public void inativarArquivo() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_INATIVAR))
				{
					this.getArquivo().setDatAlteracao(new Date());
					this.getArquivo().setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
					
					ArquivoAudienciaService.getInstancia().inativar(this.getArquivo());
					
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

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile item = event.getUploadedFile();
		UploadedImage file = new UploadedImage();
		file.setLength(item.getData().length);
		file.setName(item.getName());
		file.setData(item.getData());
		files.add(file);
		
		FacesMessage message = new FacesMessage("Succesful", event.getUploadedFile().getName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	@SuppressWarnings("resource")
	public void upload() {
		try {
			fileContent = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
		} catch (IOException e) {
			
		}
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

	public ArquivoAudiencia getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoAudiencia arquivo) {
		this.arquivo = arquivo;
	}

//	public UploadedImage getFile() {
//		return file;
//	}
//
//	public void setFile(UploadedImage file) {
//		this.file = file;
//	}

	public ArrayList<UploadedImage> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UploadedImage> files) {
		this.files = files;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
}