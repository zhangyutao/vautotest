package basic;

public interface Client {
	<T> T getResponse();

	void close();

	<T> void execute(T content);
}
