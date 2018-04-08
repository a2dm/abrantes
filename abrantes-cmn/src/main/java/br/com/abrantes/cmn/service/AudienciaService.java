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

import br.com.abrantes.cmn.entity.ArquivoAudiencia;
import br.com.abrantes.cmn.entity.Audiencia;
import br.com.abrantes.cmn.entity.Parametro;
import br.com.abrantes.cmn.util.A2DMHbNgc;
import br.com.abrantes.cmn.util.HibernateUtil;
import br.com.abrantes.cmn.util.RestritorHb;
import br.com.abrantes.cmn.util.jsf.JSFUtil;

public class AudienciaService extends A2DMHbNgc<Audiencia>
{
	private static AudienciaService instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	public static final int JOIN_ARQUIVO = 4;
	
	private JSFUtil util = new JSFUtil();
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static AudienciaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new AudienciaService();
		}
		return instancia;
	}
	
	public AudienciaService()
	{
		adicionarFiltro("idAudiencia", RestritorHb.RESTRITOR_EQ,"idAudiencia");
		adicionarFiltro("datAudiencia", RestritorHb.RESTRITOR_EQ,"datAudiencia");
		adicionarFiltro("vara", RestritorHb.RESTRITOR_LIKE, "vara");
		adicionarFiltro("processo", RestritorHb.RESTRITOR_EQ, "processo");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("listArquivo.flgAtivo", RestritorHb.RESTRITOR_EQ, "filtroMap.flgArquivoAtivo");
	}
	
	@Override
	public Audiencia inserir(Session sessao, Audiencia vo) throws Exception
	{
		List<ArquivoAudiencia> lista = new ArrayList<ArquivoAudiencia>();
		if (vo.getListArquivo() != null && vo.getListArquivo().size() > 0) {
			lista.addAll(vo.getListArquivo());
		}
		
		vo.setListArquivo(null);
		
		validarInserir(sessao, vo);
		sessao.save(vo);
		sessao.flush();
		
		if(lista != null)
		{
			for (ArquivoAudiencia obj : lista)
			{
				obj.setIdAudiencia(vo.getIdAudiencia());
				obj.setIdUsuarioCad(vo.getIdUsuarioCad());
				obj.setDatCadastro(new Date());
				obj.setFlgAtivo("S");
				obj.setDesArquivo(obj.getNome());
				
				ArquivoAudienciaService.getInstancia().inserir(sessao, obj);
			}
		}
		
		return vo;
	}
	
	public Audiencia alterarAnexo(Audiencia vo) throws Exception
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
	
	public Audiencia alterarAnexo(Session sessao, Audiencia vo) throws Exception 
	{
		List<ArquivoAudiencia> lista = new ArrayList<ArquivoAudiencia>();
		lista.addAll(vo.getListArquivo());

		ArquivoAudiencia arquivoAudiencia = new ArquivoAudiencia();
		arquivoAudiencia.setIdAudiencia(vo.getIdAudiencia());
		
		List<ArquivoAudiencia> listArquivos = ArquivoAudienciaService.getInstancia().pesquisar(sessao, arquivoAudiencia, 0);
		
		if (listArquivos != null && listArquivos.size() > 0) 
		{
			Parametro parametro = new Parametro();
			parametro.setDescricao("FILES_AUDIENCIA");
			parametro = ParametroService.getInstancia().get(sessao, parametro, 0);
			
			for (ArquivoAudiencia element : listArquivos) {
				element.setFlgAtivo("N");
				element.setIdUsuarioAlt(element.getIdUsuarioAlt());
				element.setDatAlteracao(new Date(0));
				
				ArquivoAudienciaService.getInstancia().alterar(sessao, element);
				this.excluirFileDiretorio(element, parametro, File.separator, lista);
			}
		}
		
		if (lista != null)
		{
			for (ArquivoAudiencia obj : lista)
			{
				obj.setIdAudiencia(vo.getIdAudiencia());
				obj.setIdUsuarioCad(vo.getIdUsuarioCad());
				obj.setDatCadastro(new Date());
				obj.setFlgAtivo("S");
				obj.setDesArquivo(obj.getNome());
				
				ArquivoAudienciaService.getInstancia().inserir(sessao, obj);
				this.salvarFileDiretorio(sessao, obj);
			}
		}
		return vo;
	}
	
	private void excluirFileDiretorio(ArquivoAudiencia element, Parametro parametro, String barra, List<ArquivoAudiencia> lista) throws Exception {
		boolean achouElement = false;
		
		for (ArquivoAudiencia obj : lista ) {
			if (element.getNome().trim().equalsIgnoreCase(obj.getNome().trim())) {
				achouElement = true;
				break;
			}
		}
		
		if (!achouElement) {
			Files.deleteIfExists(Paths.get(parametro.getValor() + barra + element.getIdAudiencia() + barra + element.getNome()));
		}
	}
	
	public ArquivoAudiencia salvarFileDiretorio(ArquivoAudiencia vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = salvarFileDiretorio(sessao, vo);
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
	
	public void excluirFileDiretorio(ArquivoAudiencia file) throws Exception {
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			excluirFileDiretorio(sessao, file);
			tx.commit();
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
	
	public void excluirFileDiretorio(Session sessao, ArquivoAudiencia file) throws Exception 
	{
		ArquivoAudiencia arquivoAudiencia = new ArquivoAudiencia();
		arquivoAudiencia.setIdArquivoAudiencia(file.getIdArquivoAudiencia());
		arquivoAudiencia = ArquivoAudienciaService.getInstancia().get(sessao, arquivoAudiencia, 0);
		
		arquivoAudiencia.setFlgAtivo("N");
		arquivoAudiencia.setIdUsuarioAlt(file.getIdUsuarioAlt());
		arquivoAudiencia.setDatAlteracao(new Date(0));
		
		ArquivoAudienciaService.getInstancia().alterar(sessao, arquivoAudiencia);
		
		Parametro parametro = new Parametro();
		parametro.setDescricao("FILES_AUDIENCIA");
		parametro = ParametroService.getInstancia().get(sessao, parametro, 0);
		
		Files.deleteIfExists(Paths.get(parametro.getValor() + File.separator + arquivoAudiencia.getIdAudiencia() + File.separator + arquivoAudiencia.getNome()));
	}

	public ArquivoAudiencia salvarFileDiretorio(Session sessao, ArquivoAudiencia file) throws Exception
	{
		ArquivoAudienciaService.getInstancia().inserir(sessao, file);
		
		Parametro parametro = new Parametro();
		parametro.setDescricao("FILES_AUDIENCIA");
		parametro = ParametroService.getInstancia().get(sessao, parametro, 0);
		
		File folder = new File(parametro.getValor() + File.separator + file.getIdAudiencia());
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
		return file;
	}	
	
	public Audiencia inativar(Audiencia vo) throws Exception
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

	public Audiencia inativar(Session sessao, Audiencia vo) throws Exception
	{
		Audiencia audiencia = new Audiencia();
		audiencia.setIdAudiencia(vo.getIdAudiencia());
		audiencia = this.get(sessao, audiencia, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	public Audiencia ativar(Audiencia vo) throws Exception
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
	
	public Audiencia ativar(Session sessao, Audiencia vo) throws Exception
	{
		Audiencia audiencia = new Audiencia();
		audiencia.setIdAudiencia(vo.getIdAudiencia());
		audiencia = this.get(sessao, audiencia, 0);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Audiencia.class);

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
		
	@Override
	protected void setarOrdenacao(Criteria criteria, Audiencia vo, int join)
	{
		criteria.addOrder(Order.desc("datAudiencia"));
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
