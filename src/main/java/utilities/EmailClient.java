package utilities;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import basic.Client;
import basic.EmailProvider;

import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailClient implements Client {

	private String prot, mailhost;
	private Session sess;
	private String response;

	public EmailClient(EmailProvider protocol, String mailhost) {
		if (protocol != null & !protocol.equals("") & mailhost != null & !mailhost.equals("")) {

			this.prot = getProtocol(protocol);
			this.mailhost = mailhost;
			Properties properties = System.getProperties();
			properties.setProperty("mail." + this.prot + ".host", this.mailhost);
			properties.setProperty("mail." + this.prot + ".auth", "false");
			this.sess = Session.getInstance(properties, null);

		} else {
			throw new IllegalArgumentException("mailSever cannot be null or blank");
		}

	}

	public EmailClient(EmailProvider protocol, String mailhost, String user, String password) {
		if (protocol != null & !protocol.equals("") & mailhost != null & !mailhost.equals("")) {

			this.prot = getProtocol(protocol);
			this.mailhost = mailhost;
			Properties properties = System.getProperties();
			properties.setProperty("mail." + this.prot + ".host", this.mailhost);
			properties.setProperty("mail." + this.prot + ".auth", "true");

			this.sess = Session.getDefaultInstance(properties, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, password);
				}
			});

		} else {
			throw new IllegalArgumentException("mailSever cannot be null or blank");
		}

	}

	private String getProtocol(EmailProvider protocol) {
		String prot = "";
		switch (protocol) {
		case SMTP:
			prot = "smtp";
			break;
		case SMTPS:
			prot = "smtps";
			break;
		/*
		 * case "imap": prot = "imap"; break; case "pop3": prot = "pop3"; break;
		 */
		default:
			prot = "smtp";
		}
		return prot;
	}

	/**
	 * 
	 * @param mailhost
	 *            the email smtp server like "smtp3.hp.com"
	 * @param from
	 *            the email address
	 * @param to
	 *            the email address. if there are many email address, please use
	 *            comma like "abc@hp.com, efg@hp.com";
	 * @param subject
	 *            the subject of email
	 * @param textbody
	 *            the test body of the email
	 * @param attachmentsPath
	 *            the file would be attached in the email
	 */

	public void sendEmail(String from, String to, String subject, String textbody, String[] attachmentsPath) {

		try {

			// send email
			execute(setupMessage(from, to, subject, textbody, attachmentsPath));
			System.out.println("Sent email successfully.");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}

	}

	public Message setupMessage(String from, String to, String subject, String textbody, String[] attachmentsPath)
			throws AddressException, MessagingException {
		if (from != null & !from.equals("") & to != null & !to.equals("") & subject != null & !subject.equals("")) {

			MimeMessage msg = new MimeMessage(sess);

			// set from
			msg.setFrom(new InternetAddress(from));
			// set to
			msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			msg.setSubject(subject);
			// build full content
			Multipart multipart = new MimeMultipart();

			// set body text
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(textbody);

			multipart.addBodyPart(textPart);
			if (!attachmentsPath.equals("")) {
				for (String path : attachmentsPath) {
					// set attched file
					MimeBodyPart attchPart = new MimeBodyPart();
					// String filename =
					try {
						attchPart.attachFile(path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					multipart.addBodyPart(attchPart);
				}

			}

			// set content
			msg.setContent(multipart);
			return msg;
		} else {
			throw new IllegalArgumentException("mailhost cannot be null or blank");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getResponse() throws Exception {
		// TODO Auto-generated method stub
		return this.response;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Object content) throws Exception {
		// TODO Auto-generated method stub
		try {
			Message msg = (Message) content;
			msg.saveChanges();
			Transport.send(msg, msg.getAllRecipients());

			this.response = "Sent email successfully.";

		} catch (Exception e) {
			this.response = e.getMessage();
			e.printStackTrace();
		}
	}

}
