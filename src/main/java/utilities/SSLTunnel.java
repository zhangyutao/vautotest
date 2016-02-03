package utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

import org.testng.annotations.Test;

import com.hp.ecs.ssh.SecureShellClient;
import com.hp.ecs.ssh.SecureShellClientException;

/**
 * use to setup SSL tunnel.
 * 
 * @author zhangyutao
 *
 */
public class SSLTunnel implements Runnable {

	private SecureShellClient ssc;
	private SSLTunnel SSLTunnelOne;
	private String jumpstaionIP = "";
	private String jumpstaionUser = "";
	private String jumpstaionIPPassword = "";
	private String proposeTunnelIP = "";
	private int proposeTunnelPort;
	private String destServer = "";
	private int destPort;
	private String sslTunnelStatus = "-1";// -1:Not
											// Ready,0:Good,1:Closed,2:ErrorOnBuild,3:ErrorOnClose.
	private Thread mythread;
	private ServerSocket ss;

	public SSLTunnel() {

	}

	public String getStatus() {
		switch (SSLTunnelOne.sslTunnelStatus) {
		case "-1":
			return "Not Ready";
		case "0":
			return "Good";
		case "1":
			return "Closed";
		case "2":
			return "ErrorOnBuild";
		case "3":
			return "ErrorOnClose";
		default:
			return "Exception";
		}

	}

	@Test
	private void test() {
		try {
			setupSSLTunnel("127.0.0.1", 9997, "204.104.5.221", "hurui", "Sfv19830829V", "204.104.5.78", 22);
			System.out.println(getStatus());
			closeSSLTunnel();
			System.out.println(getStatus());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

	/**
	 * setup a SSL Tunnel
	 * 
	 * @param proposeTunnelIP
	 *            - the ip address which would be listened and forward to the
	 *            destined server.
	 * @param proposeTunnelPort
	 *            - the port which would be listened and forward to the destined
	 *            server.
	 * @param jumpstaionIP
	 *            - the jumpstation server's IP through which you can connect to
	 *            the destined server.
	 * @param jumpstaionUser
	 *            - the user of jumpstation server.
	 * @param jumpstaionIPPassword
	 *            - the password of jumpstation server.
	 * @param destServer
	 *            - the destined server you want to access
	 * @param destPort
	 *            - the port of destined server
	 * @throws Exception
	 */
	public void setupSSLTunnel(String proposeTunnelIP, int proposeTunnelPort, String jumpstaionIP,
			String jumpstaionUser, String jumpstaionIPPassword, String destServer, int destPort) throws Exception {

		SSLTunnelOne = new SSLTunnel();
		SSLTunnelOne.setTunnelInfo(proposeTunnelIP, proposeTunnelPort, jumpstaionIP, jumpstaionUser,
				jumpstaionIPPassword, destServer, destPort);
		Thread th = new Thread(SSLTunnelOne);
		th.setDaemon(true);
		th.start();
		Date startTime = new Date();
		boolean ts = false;
		while (SSLTunnelOne.ssc == null || SSLTunnelOne.ssc.isLocalForwardStart == null) {
			Thread.sleep(30);
			Date curTime = new Date();
			if (Utility.dateDiff(startTime, curTime, "yyyy-MM-dd HH:mm:ss", "s") >= 60) {
				ts = true;
				break;
			}
		}
		if (!ts) {
			while (SSLTunnelOne.ssc != null && SSLTunnelOne.ssc.isLocalForwardStart != null
					&& SSLTunnelOne.ssc.isLocalForwardStart.equals("0") && SSLTunnelOne.sslTunnelStatus.equals("-1")) {
				Thread.sleep(30);
				Date curTime = new Date();
				if (Utility.dateDiff(startTime, curTime, "yyyy-MM-dd HH:mm:ss", "s") >= 60) {
					ts = true;
					break;
				}
			}
		}

		if (ts) {
			closeSSLTunnel();
			SSLTunnelOne.sslTunnelStatus = "2";
			throw new Exception("Cannot setup SSL Tunnel. time out : 60 sec");

		} else if (SSLTunnelOne.ssc.isLocalForwardStart.equals("-1")) {
			closeSSLTunnel();
			SSLTunnelOne.sslTunnelStatus = "2";
			throw new Exception("Failed to setup SSL Tunnel.");

		} else if (SSLTunnelOne.sslTunnelStatus.equals("-1")) {

			for (int w = 1; w < 100; w++) {
				Thread.sleep(20);
			}
			SSLTunnelOne.sslTunnelStatus = "0";
			System.out.println("SSL Tunnel is setup.");

		}
	}

	public void closeSSLTunnel() throws Exception {
		System.out.println("Closing SSL Tunnel.");
		if (SSLTunnelOne != null) {
			SSLTunnelOne.close();
			SSLTunnelOne.sslTunnelStatus = "1";
		}
		if (SSLTunnelOne.ssc != null) {
			try {
				SSLTunnelOne.ssc.disconnectSSHClient();
			} catch (SecureShellClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SSLTunnelOne.sslTunnelStatus = "3";
				throw new Exception("Exception when closing ssh client");
			}
		}
	}

	@SuppressWarnings("unused")
	private void setTunnelInfo(String destServer, int destPort) {
		this.sslTunnelStatus = "-1";
		this.proposeTunnelIP = destServer;
		this.proposeTunnelPort = destPort;
	}

	private void setTunnelInfo(String proposeTunnelIP, int proposeTunnelPort, String jumpstaionIP,
			String jumpstaionUser, String jumpstaionIPPassword, String destServer, int destPort) {
		try {
			ss = new ServerSocket();
		} catch (Exception e1) {
			// TODO Auto-generated catch block

			System.out.println("SecureShellClientException:" + e1.getMessage());
		}
		if (this.ssc != null) {
			try {
				this.ssc.disconnectSSHClient();
			} catch (SecureShellClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("SecureShellClientException:" + e.getMessage());
			}
		}
		this.sslTunnelStatus = "-1";
		this.jumpstaionIP = jumpstaionIP;
		this.jumpstaionUser = jumpstaionUser;
		this.jumpstaionIPPassword = jumpstaionIPPassword;
		this.proposeTunnelIP = proposeTunnelIP;
		this.proposeTunnelPort = proposeTunnelPort;
		this.destServer = destServer;
		this.destPort = destPort;

	}

	private void close() {
		try {
			if (ss != null && !ss.isClosed()) {
				ss.close();
			}

			if (mythread != null && mythread.isAlive()) {
				mythread.interrupt();
			}
			if (this.ssc != null) {
				this.ssc.disconnectSSHClient();
				this.ssc = null;
			}
			System.out.println("SSL tunnel is closed");
			this.sslTunnelStatus = "1";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			this.sslTunnelStatus = "3";
		}
	}

	public void run() {
		this.mythread = Thread.currentThread();
		try {
			this.ssc = new SecureShellClient(jumpstaionIP, jumpstaionUser, jumpstaionIPPassword);
			this.ss = ssc.serverSocket;
			System.out.println("Setting up SSL tunnel");
			this.ssc.localForward(proposeTunnelIP, proposeTunnelPort, destServer, destPort);

			// ssh.remoteForward(jumpstaionIP, jumpstaionPortForAccept,
			// proposeTunnelIP, proposeTunnelPort);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (ssc != null && ssc.serverSocket != null && !ssc.serverSocket.isClosed()) {
				try {
					ssc.serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}
			}
			if (ssc != null) {
				try {
					ssc.disconnectSSHClient();
				} catch (SecureShellClientException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}
			}
			this.sslTunnelStatus = "2";
			System.out.println("SecureShellClientException:" + e.getMessage());
			Date startTime = new Date();
			Date curTime = new Date();
			while (Utility.dateDiff(startTime, curTime, "yyyy-MM-dd HH:mm:ss", "s") < 10) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}
				curTime = new Date();
			}
		}

	}
}
