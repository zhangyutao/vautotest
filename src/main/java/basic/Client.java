package basic;

public interface Client {
	<T> T getResponse() throws Exception;

	void close() throws Exception;

	<T> void execute(T content) throws Exception;
}
