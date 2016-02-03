package basic;

import javax.mail.Message;

/**
 * this class would support all email format
 * 
 * @author yu-tao.zhang@hpe.com
 *
 */
public interface DefaultEmailFormat {
	public Message getMessage();

	public Message[] getMessages();

	public void setupMessage(String from, String to, String subject, String textbody, String[] pathOfAttachments);

	public void setupMessages(Message[] msgs);

	public Client getClient();

	public void setClient(Client cc);

	public void execute();

	public String getResponse();

	public String[] getResponses();
}
