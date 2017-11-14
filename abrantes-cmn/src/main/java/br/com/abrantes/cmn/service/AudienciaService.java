package br.com.abrantes.cmn.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
	}
	
	@Override
	public Audiencia inserir(Session sessao, Audiencia vo) throws Exception
	{
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		List<ArquivoAudiencia> lista = new ArrayList<ArquivoAudiencia>();
		lista.addAll(vo.getListArquivo());
		
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
				obj.setDesArquivo(request.getRequestedSessionId() + String.valueOf(Math.random() * 10000) + obj.getNome());
				
				ArquivoAudienciaService.getInstancia().inserir(sessao, obj);
				this.salvarFileDiretorio(obj);
			}
		}
		
		return vo;
	}
	
	public void salvarFileDiretorio(ArquivoAudiencia file) throws Exception
	{
		Parametro parametro = new Parametro();
		parametro.setDescricao("FILES_AUDIENCIA");
		parametro = ParametroService.getInstancia().get(parametro, 0);
		
		String nomeArquivoSaida = parametro.getValor() + file.getDesArquivo();

        try (InputStream is = file.getFile().getInputStream();
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
