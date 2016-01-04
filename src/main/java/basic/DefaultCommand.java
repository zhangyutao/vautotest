package basic;

import java.util.ArrayList;

/**
 * this class would support all String type's command
 * 
 * @author Linus
 *
 */

public interface DefaultCommand {
	String content = "";

	String getLine();

	void setLine(String arg);

	String[] getLines();

	void setLines(String[] arg);

	Client getClient();

	void setClient(Client cc);

	void execute() throws Exception;

	<T> T getResponse() throws Exception;

	<T> ArrayList<T> getResponses() throws Exception;
}
