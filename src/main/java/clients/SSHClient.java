package clients;

import com.hp.ecs.ssh.SecureShellClient;
import com.hp.ecs.ssh.SecureShellClientException;

import basic.Client;
import utilities.SSLTunnel;

/**
 * the client used to execute a {@link requests.CommandRequest} which belongs to
 * SSH command and also can be single instated to handle SSH command
 * 
 * @author zhangyutao
 *
 */
public class SSHClient implements Client {

	private SecureShellClient client;

	private String result = "";

	public SSHClient() {
	}

	public void openSession(String hostname, String uname, String pwd) throws SecureShellClientException {
		client = new SecureShellClient(hostname, uname, pwd);
	}

	public void openSession(String hostname, int port, String uname, String pwd) throws SecureShellClientException {
		client = new SecureShellClient(hostname, port, uname, pwd);
	}

	public SSLTunnel buildSSLTunnel(String proposeTunnelIP, int proposeTunnelPort, String jumpstaionIP,
			String jumpstaionUser, String jumpstaionIPPassword, String destServer, int destPort) throws Exception {
		SSLTunnel ssltl = new SSLTunnel();
		ssltl.setupSSLTunnel(proposeTunnelIP, proposeTunnelPort, jumpstaionIP, jumpstaionUser, jumpstaionIPPassword,
				destServer, destPort);
		return ssltl;
	}

	@Override
	public void execute(Object commandline) throws Exception {

		System.out.println("starts to execute:" + commandline);
		try {
			result = client.execCMD((String) commandline);
		} catch (SecureShellClientException e) {
			System.out.println(e.getMessage());
		}
		if (result == null) {
			throw new Exception("No result is returned");
		} else {
			System.out.println("Result is returned");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getResponse() throws Exception {
		return result;
	}

	@Override
	public void close() throws Exception {
		if (client != null) {
			client.disconnectSSHClient();
		}

	}
}
