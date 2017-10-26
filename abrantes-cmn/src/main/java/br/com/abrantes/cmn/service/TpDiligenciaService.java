package br.com.abrantes.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.abrantes.cmn.entity.TpDiligencia;
import br.com.abrantes.cmn.util.A2DMHbNgc;

public class TpDiligenciaService extends A2DMHbNgc<TpDiligencia>
{
	private static TpDiligenciaService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static TpDiligenciaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new TpDiligenciaService();
		}
		return instancia;
	}
	
	public TpDiligenciaService()
	{
		
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(TpDiligencia.class);
		
		return criteria;
	}
		
	@Override
	protected void setarOrdenacao(Criteria criteria, TpDiligencia vo, int join)
	{
		criteria.addOrder(Order.asc("desTpDiligencia"));
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
