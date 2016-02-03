package requests;

import basic.Client;
import basic.DefaultServerCheckpoint;
import basic.E2ECheckpointElements;
import clients.E2EValidationClient;

/**
 * this class is for defining E2E validation request which can be identified by
 * {@link factories.RequestsFactory} and it can be annotated by
 * {@link annotations.request.e2evalidation.Items}.
 * 
 * @author zhangyutao
 *
 */
public class E2EValidationRequest implements DefaultServerCheckpoint {
	private E2ECheckpointElements content = null;

	private E2EValidationClient e2eValidationClient = null;
	private String result = null;

	@Override
	public E2ECheckpointElements getElements() {
		// TODO Auto-generated method stub
		return this.content;
	}

	@Override
	public void setElements(E2ECheckpointElements arg) {
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
	public void execute() {
		// TODO Auto-generated method stub
		if (this.content != null) {
			try {
				this.e2eValidationClient.execute(this.content);
				this.result = this.e2eValidationClient.getResponse();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} else {
			System.out.println("Nothing to execute!");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getResult() {
		// TODO Auto-generated method stub
		return this.result;

	}

}
