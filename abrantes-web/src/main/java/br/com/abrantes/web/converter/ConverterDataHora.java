package br.com.abrantes.web.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.abrantes.cmn.util.jsf.JSFUtil;

@FacesConverter("converterDataHora")
public class ConverterDataHora implements Converter
{
	public Object getAsObject(FacesContext contexto, UIComponent componente, String valor)
	{
		Date data = JSFUtil.converteStringData(valor);
		return data;
    }

	public String getAsString(FacesContext contexto, UIComponent componente, Object objeto)
	{
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formato.format(objeto);
	}
}