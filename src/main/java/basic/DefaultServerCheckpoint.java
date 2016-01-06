package basic;

/**
 * this class would support all String type's command
 * 
 * @author Linus
 *
 */

public interface DefaultServerCheckpoint {
	ServerCheckpointElements content = null;

	ServerCheckpointElements getElements();

	void setElements(ServerCheckpointElements arg);

	Client getClient();

	void setClient(Client cc);

	void execute() throws Exception;

	<T> T getResult() throws Exception;

}
