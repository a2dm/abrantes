package br.com.abrantes.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.abrantes.cmn.entity.TpContratacao;
import br.com.abrantes.cmn.util.A2DMHbNgc;

public class TpContratacaoService extends A2DMHbNgc<TpContratacao>
{
	private static TpContratacaoService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static TpContratacaoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new TpContratacaoService();
		}
		return instancia;
	}
	
	public TpContratacaoService()
	{
		
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(TpContratacao.class);
		
		return criteria;
	}
		
	@Override
	protected void setarOrdenacao(Criteria criteria, TpContratacao vo, int join)
	{
		criteria.addOrder(Order.asc("desTpContratacao"));
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
