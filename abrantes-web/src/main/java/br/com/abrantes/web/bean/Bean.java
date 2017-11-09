package br.com.abrantes.web.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

public class Bean {
	
	private Part file;
	
	private String fileContent;

	@SuppressWarnings("resource")
	public void upload() {
		try {
			fileContent = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
		} catch (IOException e) {
			
		}
	}

	public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		if (file.getSize() > 1024) {
			msgs.add(new FacesMessage("file too big"));
		}
		if (!"text/plain".equals(file.getContentType())) {
			msgs.add(new FacesMessage("not a text file"));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}
}
