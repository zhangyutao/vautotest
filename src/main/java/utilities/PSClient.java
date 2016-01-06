package utilities;

import com.hp.ecs.tools.powershell.PSCException;
import com.hp.ecs.tools.powershell.PowerShellClient;

import basic.Client;

/**
 * the client used to execute PowerShell command.
 * 
 * @author zhangyutao
 *
 */
public class PSClient implements Client {

	private String result = "";
	private int defautTimeOut = 120;// seconds

	public PSClient() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getResponse() throws Exception {
		return result;
	}

	@Override
	public void execute(Object commandline) throws PSCException {
		String[] commandlines = { (String) commandline };
		execute(commandlines, defautTimeOut);
	}

	/**
	 * execute powsershell commandline in local windows. author:
	 * zhangyutao Created on Mar 11, 2014
	 * 
	 * @param timeOutInSeconds
	 *            the time out to wait the execution
	 * @throws PSCException
	 */
	public void execute(String[] commandline, int timeOutInSeconds) throws PSCException {
		checkPSIsInstalled();
		System.out.println("start to execute: " + stringAttraytoString(commandline));
		PowerShellClient ps = new PowerShellClient();
		result = ps.invokePowershellCommand(commandline, timeOutInSeconds);
		// Reporter.reportAMessage("PS Command execution end");
		if (result == null) {
			System.out.println("Cannot get result");
		} else {
			System.out.println("Got result");
		}
	}

	/**
	 * execute powsershell commandline in remote windows computer. The time out
	 * to wait the execution is {@link #defautTimeOut} seconds. author:
	 * zhangyutao Created on Mar 11, 2014
	 * 
	 * @param targetHost
	 *            remote computer's ip or remote computer name
	 * @param username
	 *            remote computer's username
	 * @param password
	 *            remote computer's password
	 * @throws PSCException
	 */
	public void executeOnRemote(String[] commandline, String targetHost, String username, String password)
			throws PSCException {
		executeOnRemote(commandline, targetHost, username, password, defautTimeOut);
	}

	/**
	 * execute powsershell commandline in remote windows computer. author:
	 * zhangyutao Created on Mar 11, 2014
	 * 
	 * @param targetHost
	 *            remote computer's ip or remote computer name
	 * @param username
	 *            remote computer's username
	 * @param password
	 *            remote computer's password
	 * @param timeOutInSeconds
	 *            timeOutInSeconds
	 * @throws PSCException
	 */
	public void executeOnRemote(String[] commandline, String targetHost, String username, String password,
			int timeOutInSeconds) throws PSCException {
		// enablePSRM();
		System.out.println("Start to execute on Host:" + targetHost + "(UserName:" + username + " PWD:" + password
				+ ")\nCommands:" + stringAttraytoString(commandline));
		PowerShellClient ps = new PowerShellClient();
		result = ps.invokePowershellCommandOn1stRemote(targetHost, username, password, commandline, timeOutInSeconds);
		if (result == null) {
			System.out.println("Cannot get result");
		} else {
			System.out.println("Got result");
		}

	}

	/**
	 * execute powsershell commandline in remote windows computer. The time out
	 * to wait the execution is {@link #defautTimeOut} seconds. author:
	 * zhangyutao Created on Nov 04, 2014
	 * 
	 * @param commandline
	 * @param targetHost1
	 *            jumpstation's ip or remote computer name
	 * @param username1
	 *            jumpstation's username
	 * @param password1
	 *            jumpstation's password
	 * @param targetHost2
	 *            remote computer's ip or remote computer name
	 * @param username2
	 *            remote computer's username
	 * @param password2
	 *            remote computer's password
	 * @throws PSCException
	 */
	public void executeOnRemoteByJS(String[] commandline, String targetHost1, String username1, String password1,
			String targetHost2, String username2, String password2) throws PSCException {
		executeOnRemoteByJS(commandline, targetHost1, username1, password1, targetHost2, username2, password2,
				defautTimeOut);
	}

	/**
	 * execute powsershell commandline in remote windows computer. author:
	 * zhangyutao Created on Nov 04, 2014
	 * 
	 * @param commandline
	 * @param targetHost1
	 *            jumpstation's ip or remote computer name
	 * @param username1
	 *            jumpstation's username
	 * @param password1
	 *            jumpstation's password
	 * @param targetHost2
	 *            remote computer's ip or remote computer name
	 * @param username2
	 *            remote computer's username
	 * @param password2
	 *            remote computer's password
	 * @throws PSCException
	 */
	public void executeOnRemoteByJS(String[] commandline, String targetHost1, String username1, String password1,
			String targetHost2, String username2, String password2, int timeOutInSeconds) throws PSCException {
		// enablePSRM();
		System.out.println("Start to execute on Host:" + targetHost2 + "(UserName:" + username2 + " PWD:" + password2
				+ ")from Host:" + targetHost1 + "\nCommands:" + stringAttraytoString(commandline));
		PowerShellClient ps = new PowerShellClient();

		result = ps.invokeRemotePowershellCommandOn2ndRemote(targetHost1, username1, password1, targetHost2, username2,
				password2, commandline, timeOutInSeconds);

		if (result == null) {
			System.out.println("Cannot get result");
		} else {
			System.out.println("Got result");
		}

	}

	/**
	 * transform string array to a string. author: zhangyutao Created
	 * on Mar 11, 2014
	 * 
	 * @param a
	 *            array
	 * @return a string
	 */
	private String stringAttraytoString(String[] a) {
		String line = "";
		if (a != null) {
			for (int i = 0; i < a.length; i++) {
				line = line + a[i] + "\n";
			}
			line = line.substring(0, line.length() - "\n".length());
		}
		return line;
	}

	/**
	 * check if PS is installed. author: zhangyutao Created on Mar 11,
	 * 2014
	 * 
	 * @throws PSCException
	 */
	private void checkPSIsInstalled() throws PSCException {
		PowerShellClient ps = new PowerShellClient();
		ps.checkPowershellInstalled();
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
	}

}
