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

import br.com.abrantes.cmn.entity.Diligencia;
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
