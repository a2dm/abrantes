package br.com.abrantes.cmn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

import br.com.abrantes.cmn.entity.ArquivoDiligencia;
import br.com.abrantes.cmn.util.A2DMHbNgc;
import br.com.abrantes.cmn.util.HibernateUtil;
import br.com.abrantes.cmn.util.RestritorHb;
import br.com.abrantes.cmn.util.jsf.JSFUtil;

public class ArquivoDiligenciaService extends A2DMHbNgc<ArquivoDiligencia>
{
	private static ArquivoDiligenciaService instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	private JSFUtil util = new JSFUtil();
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ArquivoDiligenciaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ArquivoDiligenciaService();
		}
		return instancia;
	}
	
	public ArquivoDiligenciaService()
	{
		adicionarFiltro("idArquivoDiligencia", RestritorHb.RESTRITOR_EQ, "idArquivoDiligencia");
		adicionarFiltro("idAudiencia", RestritorHb.RESTRITOR_EQ, "idAudiencia");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
	}
	
	public ArquivoDiligencia inativar(ArquivoDiligencia vo) throws Exception
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

	public ArquivoDiligencia inativar(Session sessao, ArquivoDiligencia vo) throws Exception
	{
		ArquivoDiligencia arquivoDiligencia = new ArquivoDiligencia();
		arquivoDiligencia.setIdArquivoDiligencia(vo.getIdArquivoDiligencia());
		arquivoDiligencia = this.get(sessao, arquivoDiligencia, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	public ArquivoDiligencia ativar(ArquivoDiligencia vo) throws Exception
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
	
	public ArquivoDiligencia ativar(Session sessao, ArquivoDiligencia vo) throws Exception
	{
		ArquivoDiligencia arquivoDiligencia = new ArquivoDiligencia();
		arquivoDiligencia.setIdArquivoDiligencia(vo.getIdArquivoDiligencia());
		arquivoDiligencia = this.get(sessao, arquivoDiligencia, 0);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(ArquivoDiligencia.class);

		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
	         criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
	         criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }
		
		return criteria;
	}
		
	@Override
	protected void setarOrdenacao(Criteria criteria, ArquivoDiligencia vo, int join)
	{
		criteria.addOrder(Order.desc("tipo"));
		criteria.addOrder(Order.asc("nome"));
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
