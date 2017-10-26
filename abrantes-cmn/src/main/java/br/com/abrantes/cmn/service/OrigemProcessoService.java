package br.com.abrantes.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.abrantes.cmn.entity.OrigemProcesso;
import br.com.abrantes.cmn.util.A2DMHbNgc;

public class OrigemProcessoService extends A2DMHbNgc<OrigemProcesso>
{
	private static OrigemProcessoService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static OrigemProcessoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new OrigemProcessoService();
		}
		return instancia;
	}
	
	public OrigemProcessoService()
	{
		
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(OrigemProcesso.class);
		
		return criteria;
	}
		
	@Override
	protected void setarOrdenacao(Criteria criteria, OrigemProcesso vo, int join)
	{
		criteria.addOrder(Order.asc("desOrigemProcesso"));
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
