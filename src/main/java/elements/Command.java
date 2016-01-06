package elements;

import java.util.ArrayList;

import basic.Client;
import basic.DefaultCommand;

/**
 * this class would support all String type's command
 * 
 * @author zhangyutao
 *
 */
public class Command implements DefaultCommand {
	private String content = null;
	private String[] contents = null;
	private Client commandClient = null;
	private Object response = null;
	private ArrayList<Object> responses = new ArrayList<Object>();

	@Override
	public String getLine() {
		// TODO Auto-generated method stub
		return this.content;
	}

	@Override
	public void setLine(String arg) {
		// TODO Auto-generated method stub
		this.content = arg;

	}

	@Override
	public String[] getLines() {
		// TODO Auto-generated method stub
		return this.contents;
	}

	@Override
	public void setLines(String[] arg) {
		// TODO Auto-generated method stub
		this.contents = arg;

	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return this.commandClient;
	}

	@Override
	public void setClient(Client cc) {
		// TODO Auto-generated method stub
		this.commandClient = cc;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		if (this.content != null) {
			this.commandClient.execute(this.content);
			this.response = this.commandClient.getResponse();
		} else if (this.contents != null) {
			for (int i = 0; i < this.contents.length; i++) {
				this.commandClient.execute(this.contents[i]);
				this.responses.add(this.commandClient.getResponse());
			}

		} else {
			System.out.println("Nothing to execute!");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getResponse() throws Exception {
		// TODO Auto-generated method stub
		return (T) this.response;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ArrayList<T> getResponses() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<T> temp = new ArrayList<T>();
		for (int i = 0; i < this.responses.size(); i++) {
			temp.add((T) this.responses.get(i));
		}
		return temp;
	}

}
