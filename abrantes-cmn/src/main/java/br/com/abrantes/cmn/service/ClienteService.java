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

import br.com.abrantes.cmn.entity.Cliente;
import br.com.abrantes.cmn.util.A2DMHbNgc;
import br.com.abrantes.cmn.util.HibernateUtil;
import br.com.abrantes.cmn.util.RestritorHb;
import br.com.abrantes.cmn.util.jsf.JSFUtil;

public class ClienteService extends A2DMHbNgc<Cliente>
{
	private static ClienteService instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	public static final int JOIN_ESTADO = 4;
	
	private JSFUtil util = new JSFUtil();
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ClienteService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ClienteService();
		}
		return instancia;
	}
	
	public ClienteService()
	{
		adicionarFiltro("idCliente", RestritorHb.RESTRITOR_EQ,"idCliente");
		adicionarFiltro("idCliente", RestritorHb.RESTRITOR_NE, "filtroMap.idClienteNotEq");
		adicionarFiltro("desCliente", RestritorHb.RESTRITOR_LIKE, "desCliente");
		adicionarFiltro("desCliente", RestritorHb.RESTRITOR_EQ, "filtroMap.desCliente");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");		
	}
	
	@Override
	protected void validarInserir(Session sessao, Cliente vo) throws Exception
	{
		Cliente cliente = new Cliente();
		cliente.setFlgAtivo("S");		
		cliente.setFiltroMap(new HashMap<String, Object>());
		cliente.getFiltroMap().put("desCliente", vo.getDesCliente().trim());		
		
		cliente = this.get(sessao, cliente, 0);
		
		if(cliente != null)
		{
			throw new Exception("Este cliente já está cadastrado na sua base de dados!");
		}
	}
	
	@Override
	public Cliente inserir(Session sessao, Cliente vo) throws Exception
	{
		validarInserir(sessao, vo);
		sessao.save(vo);
		
		return vo;
	}
	
	@Override
	protected void validarAlterar(Session sessao, Cliente vo) throws Exception
	{
		Cliente cliente = new Cliente();
		cliente.setFiltroMap(new HashMap<String, Object>());
		cliente.getFiltroMap().put("idClienteNotEq", vo.getIdCliente());
		cliente.getFiltroMap().put("desCliente", vo.getDesCliente().trim());
		cliente.setFlgAtivo(vo.getFlgAtivo());		
		
		cliente = this.get(sessao, cliente, 0);
		
		if(cliente != null)
		{
			throw new Exception("Este cliente já está cadastrado na sua base de dados!");
		}
	}
	
	@Override
	public Cliente alterar(Session sessao, Cliente vo) throws Exception
	{
		validarAlterar(sessao, vo);
		sessao.merge(vo);		
				
		return vo;
	}
	
	public Cliente inativar(Cliente vo) throws Exception
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

	public Cliente inativar(Session sessao, Cliente vo) throws Exception
	{
		Cliente cliente = new Cliente();
		cliente.setIdCliente(vo.getIdCliente());
		cliente = this.get(sessao, cliente, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	public Cliente ativar(Cliente vo) throws Exception
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
	
	public Cliente ativar(Session sessao, Cliente vo) throws Exception
	{
		Cliente cliente = new Cliente();
		cliente.setIdCliente(vo.getIdCliente());
		cliente = this.get(sessao, cliente, 0);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Cliente.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
	         criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
			criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }
		
		if ((join & JOIN_ESTADO) != 0)
	    {
	         criteria.createAlias("estado", "estado", JoinType.LEFT_OUTER_JOIN);
	    }
		
		return criteria;
	}
		
	@Override
	protected void setarOrdenacao(Criteria criteria, Cliente vo, int join)
	{
		criteria.addOrder(Order.asc("desCliente"));
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
