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

import br.com.abrantes.cmn.entity.ArquivoAudiencia;
import br.com.abrantes.cmn.util.A2DMHbNgc;
import br.com.abrantes.cmn.util.HibernateUtil;
import br.com.abrantes.cmn.util.RestritorHb;
import br.com.abrantes.cmn.util.jsf.JSFUtil;

public class ArquivoAudienciaService extends A2DMHbNgc<ArquivoAudiencia>
{
	private static ArquivoAudienciaService instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	private JSFUtil util = new JSFUtil();
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ArquivoAudienciaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ArquivoAudienciaService();
		}
		return instancia;
	}
	
	public ArquivoAudienciaService()
	{
		adicionarFiltro("idArquivoAudiencia", RestritorHb.RESTRITOR_EQ, "idArquivoAudiencia");
		adicionarFiltro("idAudiencia", RestritorHb.RESTRITOR_EQ, "idAudiencia");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
	}
	
	public ArquivoAudiencia inativar(ArquivoAudiencia vo) throws Exception
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

	public ArquivoAudiencia inativar(Session sessao, ArquivoAudiencia vo) throws Exception
	{
		ArquivoAudiencia arquivoAudiencia = new ArquivoAudiencia();
		arquivoAudiencia.setIdArquivoAudiencia(vo.getIdArquivoAudiencia());
		arquivoAudiencia = this.get(sessao, arquivoAudiencia, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(ArquivoAudiencia.class);

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
	protected void setarOrdenacao(Criteria criteria, ArquivoAudiencia vo, int join)
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
