package br.com.abrantes.ngc.functions;

import br.com.abrantes.cmn.util.jsf.JSFUtil;

public class MenuControl
{
	private static JSFUtil util = new JSFUtil();
	
	private MenuControl(){}
	
	public static void ativarMenu(String desMenu)
	{
		util.getSession().removeAttribute("flgMenuDsh");
		util.getSession().removeAttribute("flgMenuAgn");
		util.getSession().removeAttribute("flgMenuCfm");
		util.getSession().removeAttribute("flgMenuRcp");
		util.getSession().removeAttribute("flgMenuAtn");
		util.getSession().removeAttribute("flgMenuAts");
		util.getSession().removeAttribute("flgMenuPct");
		util.getSession().removeAttribute("flgMenuRlt");
		util.getSession().removeAttribute("flgMenuCfg");
		
		util.getSession().setAttribute(desMenu, "active");
	}
	
	public static void ativarSubMenu(String desSubMenu)
	{
		util.getSession().removeAttribute("flgMenuRelPed");
		util.getSession().removeAttribute("flgMenuRelLog");
		util.getSession().removeAttribute("flgMenuRelObs");
		util.getSession().removeAttribute("flgMenuManUsr");
		util.getSession().removeAttribute("flgMenuManCli");
		util.getSession().removeAttribute("flgMenuManRec");
		util.getSession().removeAttribute("flgMenuManPrd");
		util.getSession().removeAttribute("flgMenuMsgAgn");
		
		util.getSession().setAttribute(desSubMenu, "active");
	}
}
