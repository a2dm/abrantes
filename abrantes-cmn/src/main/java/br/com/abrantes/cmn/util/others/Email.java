package br.com.abrantes.cmn.util.others;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email
{
	private static final String USER  = "sistema@siscob.srv.br";
	private static final String PASSWORD  = "abrantes@2018";
	
	public void enviar(String to, String assunto, String texto) throws AddressException, MessagingException 
	{
		String host = "email-ssl.com.br";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(USER, PASSWORD);
				}
			});
 
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(USER));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(to));
		message.setSubject(assunto);
		message.setText(texto);
 
		Transport.send(message);
	}
}