package br.com.abrantes.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.abrantes.cmn.entity.TpAudiencia;
import br.com.abrantes.cmn.util.A2DMHbNgc;

public class TpAudienciaService extends A2DMHbNgc<TpAudiencia>
{
	private static TpAudienciaService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static TpAudienciaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new TpAudienciaService();
		}
		return instancia;
	}
	
	public TpAudienciaService()
	{
		
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(TpAudiencia.class);
		
		return criteria;
	}
		
	@Override
	protected void setarOrdenacao(Criteria criteria, TpAudiencia vo, int join)
	{
		criteria.addOrder(Order.asc("desTpAudiencia"));
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
