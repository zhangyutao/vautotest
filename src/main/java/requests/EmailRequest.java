package requests;

import javax.mail.Message;
import basic.Client;
import basic.DefaultEmailFormat;
import clients.EmailClient;

/**
 * this class is for defining a email/emails quest which can be identified by
 * {@link factories.RequestsFactory} and it can be annotated by
 * {@link annotations.request.email.Message}.
 * 
 * @author zhangyutao
 *
 */
public class EmailRequest implements DefaultEmailFormat {

	private EmailClient client;
	private Message msg;
	private String response;
	private Message[] msgs;
	private String[] responses = null;

	@Override
	public Message getMessage() {
		// TODO Auto-generated method stub
		return this.msg;
	}

	@Override
	public void setupMessage(String from, String to, String subject, String textBody, String[] pathOfAttachments)
			throws Exception {
		this.msg = this.client.setupMessage(from, to, subject, textBody, pathOfAttachments);
	}

	@Override
	public EmailClient getClient() {
		// TODO Auto-generated method stub
		return this.client;
	}

	@Override
	public void setClient(Client cc) {
		// TODO Auto-generated method stub
		this.client = (EmailClient) cc;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		if (this.msg != null) {
			this.client.execute(this.msg);
			this.response = this.client.getResponse();
		} else if (this.msgs != null) {
			String[] tempr = new String[this.msgs.length];
			for (int i = 0; i < this.msgs.length; i++) {
				this.client.execute(this.msgs[i]);
				tempr[i] = this.client.getResponse();
			}
			this.responses = tempr;
		}

	}

	@Override
	public String getResponse() throws Exception {
		// TODO Auto-generated method stub
		return this.response;
	}

	@Override
	public Message[] getMessages() {
		// TODO Auto-generated method stub
		return this.msgs;
	}

	@Override
	public String[] getResponses() throws Exception {
		// TODO Auto-generated method stub
		return this.responses;
	}

	@Override
	public void setupMessages(Message[] msgs) {
		// TODO Auto-generated method stub
		this.msgs = msgs;
	}

}
