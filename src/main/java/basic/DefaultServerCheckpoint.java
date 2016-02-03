package basic;

/**
 * this class would support all String type's command
 * 
 * @author Linus
 *
 */

public interface DefaultServerCheckpoint {
	E2ECheckpointElements content = null;

	E2ECheckpointElements getElements();

	void setElements(E2ECheckpointElements arg);

	Client getClient();

	void setClient(Client cc);

	void execute();

	<T> T getResult();

}
