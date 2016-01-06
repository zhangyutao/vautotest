package elements;

import basic.Client;
import basic.DefaultServerCheckpoint;
import basic.ServerCheckpointElements;
import utilities.E2EValidationClient;

/**
 * this class would support all Server checkpoint
 * 
 * @author zhangyutao
 *
 */
public class ServerCheckpoint implements DefaultServerCheckpoint {
	private ServerCheckpointElements content = null;

	private E2EValidationClient e2eValidationClient = null;
	private Object result = null;

	@Override
	public ServerCheckpointElements getElements() {
		// TODO Auto-generated method stub
		return this.content;
	}

	@Override
	public void setElements(ServerCheckpointElements arg) {
		// TODO Auto-generated method stub
		this.content = arg;

	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return this.e2eValidationClient;
	}

	@Override
	public void setClient(Client cc) {
		// TODO Auto-generated method stub
		this.e2eValidationClient = (E2EValidationClient) cc;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		if (this.content != null) {
			this.e2eValidationClient.execute(this.content);
			this.result = this.e2eValidationClient.getResponse();
		} else {
			System.out.println("Nothing to execute!");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getResult() throws Exception {
		// TODO Auto-generated method stub
		return (T) this.result;

	}

}
