package br.com.abrantes.cmn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

import br.com.abrantes.cmn.entity.ArquivoDiligencia;
import br.com.abrantes.cmn.entity.Diligencia;
import br.com.abrantes.cmn.entity.Parametro;
import br.com.abrantes.cmn.util.A2DMHbNgc;
import br.com.abrantes.cmn.util.HibernateUtil;
import br.com.abrantes.cmn.util.RestritorHb;
import br.com.abrantes.cmn.util.jsf.JSFUtil;

public class DiligenciaService extends A2DMHbNgc<Diligencia>
{
	private static DiligenciaService instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	public static final int JOIN_ESTADO = 4;
	
	public static final int JOIN_ARQUIVO = 8;
	
	private JSFUtil util = new JSFUtil();
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static DiligenciaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new DiligenciaService();
		}
		return instancia;
	}
	
	public DiligenciaService()
	{
		adicionarFiltro("datPrazoMaximo", RestritorHb.RESTRITOR_EQ,"datPrazoMaximo");
		adicionarFiltro("vara", RestritorHb.RESTRITOR_LIKE, "vara");
		adicionarFiltro("processo", RestritorHb.RESTRITOR_EQ, "processo");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("listArquivo.flgAtivo", RestritorHb.RESTRITOR_EQ, "filtroMap.flgArquivoAtivo");
	}
	
	public Diligencia inativar(Diligencia vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = inativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("S");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}

	public Diligencia inativar(Session sessao, Diligencia vo) throws Exception
	{
		Diligencia diligencia = new Diligencia();
		diligencia.setIdDiligencia(vo.getIdDiligencia());
		diligencia = this.get(sessao, diligencia, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	public Diligencia ativar(Diligencia vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = ativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("N");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Diligencia ativar(Session sessao, Diligencia vo) throws Exception
	{
		Diligencia diligencia = new Diligencia();
		diligencia.setIdDiligencia(vo.getIdDiligencia());
		diligencia = this.get(sessao, diligencia, 0);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Diligencia.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
	         criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
	         criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }
		
		if ((join & JOIN_ARQUIVO) != 0)
	    {
			criteria.createAlias("listArquivo", "listArquivo", JoinType.LEFT_OUTER_JOIN);
	    }
		
		return criteria;
	}
	
	public Diligencia alterarAnexo(Diligencia vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = alterarAnexo(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Diligencia alterarAnexo(Session sessao, Diligencia vo) throws Exception 
	{
		List<ArquivoDiligencia> lista = new ArrayList<ArquivoDiligencia>();
		if (vo.getListArquivo() != null && vo.getListArquivo().size() > 0) {
			lista.addAll(vo.getListArquivo());
		}

		ArquivoDiligencia arquivoDiligencia = new ArquivoDiligencia();
		arquivoDiligencia.setIdDiligencia(vo.getIdDiligencia());
		
		List<ArquivoDiligencia> listArquivos = ArquivoDiligenciaService.getInstancia().pesquisar(sessao, arquivoDiligencia, 0);
		
		if (listArquivos != null && listArquivos.size() > 0) 
		{
			Parametro parametro = new Parametro();
			parametro.setDescricao("FILES_DILIGENCIA");
			parametro = ParametroService.getInstancia().get(sessao, parametro, 0);
			
			for (ArquivoDiligencia element : listArquivos) {
				element.setFlgAtivo("N");
				element.setIdUsuarioAlt(element.getIdUsuarioAlt());
				element.setDatAlteracao(new Date(0));
				
				ArquivoDiligenciaService.getInstancia().alterar(sessao, element);
				this.excluirFileDiretorio(element, parametro, File.separator, lista);
			}
		}
		
		if (lista != null)
		{
			for (ArquivoDiligencia obj : lista)
			{
				obj.setIdDiligencia(vo.getIdDiligencia());
				obj.setIdUsuarioCad(vo.getIdUsuarioCad());
				obj.setDatCadastro(new Date());
				obj.setFlgAtivo("S");
				obj.setDesArquivo(obj.getNome());
				
				ArquivoDiligenciaService.getInstancia().inserir(sessao, obj);
				this.salvarFileDiretorio(sessao, obj);
			}
		}
		return vo;
	}
	
	private void excluirFileDiretorio(ArquivoDiligencia element, Parametro parametro, String barra, List<ArquivoDiligencia> lista) throws Exception {
		boolean achouElement = false;
		
		for (ArquivoDiligencia obj : lista ) {
			if (element.getNome().trim().equalsIgnoreCase(obj.getNome().trim())) {
				achouElement = true;
				break;
			}
		}
		
		if (!achouElement) {
			Files.deleteIfExists(Paths.get(parametro.getValor() + barra + element.getIdDiligencia() + barra + element.getNome()));
		}
	}
	
	public void salvarFileDiretorio(Session sessao, ArquivoDiligencia file) throws Exception
	{
		Parametro parametro = new Parametro();
		parametro.setDescricao("FILES_DILIGENCIA");
		parametro = ParametroService.getInstancia().get(sessao, parametro, 0);
		
		File folder = new File(parametro.getValor() + File.separator + file.getIdDiligencia());
		String nomeArquivoSaida = folder.getPath() + File.separator + file.getNome();
		
		if (!folder.exists()) {
			folder.mkdir();
		}
		
		InputStream inputStream = null;
		
		if (file.getFile() == null) {
			inputStream = new FileInputStream(nomeArquivoSaida);
		} else {
			inputStream = file.getFile().getInputStream();
		}
		
        try (InputStream is = inputStream;
             OutputStream out = new FileOutputStream(nomeArquivoSaida))
        {
            int read = 0;
            byte[] bytes = new byte[20*1024*1024];
            
            while ((read = is.read(bytes)) != -1) 
            {
                out.write(bytes, 0, read);
            }
        } catch (IOException e) {
            throw e;
        }
	}	
		
	@Override
	protected void setarOrdenacao(Criteria criteria, Diligencia vo, int join)
	{
		criteria.addOrder(Order.desc("datPrazoMaximo"));
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Map restritores() 
	{
		return restritores;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Map filtroPropriedade() 
	{
		return filtroPropriedade;
	}
}
