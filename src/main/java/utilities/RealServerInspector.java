
package utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NodeList;

import com.hp.ecs.ssh.SecureShellClientException;

import utilities.Utility;
import basic.WinAuto;
import elements.Command;
import factories.ListFactory;
import basic.LinuxQueryList;
import basic.RealLinuxServerInfoBean;
import basic.RealWinServerInfoBean;

/**
 * a Inspector for get real server's information .
 * 
 * @author zhangyutao Created on Feb 04, 2015
 * 
 */

public class RealServerInspector {
	// private int isPSRemoting =
	// Integer.parseInt(Business.USEPSREMOTINGFORWIND);

	private boolean isFirstConnected = true;
	private WinAuto wa = null;
	private SSHClient sshc = null;
	private LinuxQueryList linuxQueryList;
	private RealLinuxServerInfoBean realServerForLinux;
	private RealWinServerInfoBean realServerForWin;

	public RealLinuxServerInfoBean getLinuxServerInfo() {
		return this.realServerForLinux;
	}

	public RealWinServerInfoBean getWinServerInfo() {
		return this.realServerForWin;
	}

	/*
	 * private PuttyOperator puttyOperator; private PuttyNetworkTunnel
	 * puttyNetworkTunnel;
	 */

	/**
	 * Check connection for Linux.
	 * 
	 * create on 2015-02-01
	 * 
	 * @param hostname
	 *            hostname
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @return the SecureShellClient object
	 * @throws Exception
	 * @throws SecureShellClientException
	 * @throws IOException
	 */
	public void connectToLinuxServer(String hostname, String username, String password) throws Exception {
		System.out.println("connecting to server: " + hostname + " user: " + username);
		if (isFirstConnected == false) {
			this.realServerForLinux = null;
			this.realServerForLinux = new RealLinuxServerInfoBean();
		} else {
			this.realServerForLinux = new RealLinuxServerInfoBean();
		}

		try {
			sshc = new SSHClient();
			sshc.openSession(hostname, username, password);
			linuxQueryList = (LinuxQueryList) ListFactory.initElements(sshc, LinuxQueryList.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Exception:" + e.getMessage());
		}

		isFirstConnected = false;

	}

	/**
	 * Check connection for Linux.
	 * 
	 * create on 2015-02-01
	 * 
	 * @param hostname
	 *            hostname
	 * @param port
	 *            port
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @return the SecureShellClient object
	 * @throws Exception
	 * @throws SecureShellClientException
	 * @throws IOException
	 */
	public void connectToLinuxServerBySSHClient(String hostname, int port, String username, String password)
			throws Exception {
		System.out.println("connecting to server: " + hostname + " port: " + port + " user: " + username);
		if (isFirstConnected == false) {
			this.realServerForLinux = null;
			this.realServerForLinux = new RealLinuxServerInfoBean();
		} else {
			this.realServerForLinux = new RealLinuxServerInfoBean();
		}

		try {
			sshc = new SSHClient();
			sshc.openSession(hostname, port, username, password);
			linuxQueryList = (LinuxQueryList) ListFactory.initElements(sshc, LinuxQueryList.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Exception:" + e.getMessage());
		}

		isFirstConnected = false;

	}

	/**
	 * Get bit size.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxBit() {
		try {
			Command query = linuxQueryList.queryBit;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxBit(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxBit(e.getMessage());
		}

	}

	/**
	 * Get CPU type.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxCPU() {

		try {
			Command query = linuxQueryList.queryCPU;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxCPU(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxCPU(e.getMessage());
		}

	}

	/**
	 * Get DNS.
	 * 
	 * create on 2015-04-20
	 */
	public void tourLinuxDNS(String ip) {

		try {
			Command query = linuxQueryList.queryDNS;
			query.setLine(query.getLine().replaceAll(LinuxQueryList.deafultIP.replaceAll("\\.", "\\\\."), ip));
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxDNS(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxDNS(e.getMessage());
		}
	}

	/**
	 * Get Floating IP.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxFloatingIP() {

		try {
			Command query = linuxQueryList.queryFloatingIP;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxFloatingIP(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxFloatingIP(e.getMessage());
		}
	}

	/**
	 * Get Floating MAC.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxFloatingMac() {

		try {
			Command query = linuxQueryList.queryFloatingMac;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxFloatingMac(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxFloatingMac(e.getMessage());
		}
	}

	/**
	 * Get host name.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxHostName() {

		try {
			Command query = linuxQueryList.queryHostname;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxHostName(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxHostName(e.getMessage());
		}
	}

	/**
	 * Get /etc/hosts.
	 * 
	 * create on 2015-04-22
	 */
	public void tourLinuxHosts() {

		try {
			Command query = linuxQueryList.queryHosts;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxHosts(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxHosts(e.getMessage());
		}
	}

	/**
	 * Get memory.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxMemory() {

		try {
			Command query = linuxQueryList.queryMemory;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxMemory(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxMemory(e.getMessage());
		}
	}

	/**
	 * Get NTP.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxNTP() {

		try {
			Command query = linuxQueryList.queryNTP;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxNTP(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxNTP(e.getMessage());
		}
	}

	/**
	 * Get OS version.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxOSVersion() {

		try {
			Command query = linuxQueryList.queryOSVersion;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxOSVersion(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxOSVersion(e.getMessage());
		}
	}

	/**
	 * Get Private IP.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxPrivateIP() {

		try {
			Command query = linuxQueryList.queryPrivateIP;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxPrivateIP(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxPrivateIP(e.getMessage());
		}
	}

	/**
	 * Get Private MAC.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxPrivateMac() {

		try {
			Command query = linuxQueryList.queryPrivateMac;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxPrivateMac(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxPrivateMac(e.getMessage());
		}
	}

	/**
	 * Get RAM.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxRAM() {

		try {
			Command query = linuxQueryList.queryRAM;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxRam(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxRam(e.getMessage());
		}
	}

	/**
	 * Get Run Level.
	 * 
	 * create on 2015-04-30
	 */
	public void tourLinuxRunLevel() {
		try {
			Command query = linuxQueryList.queryRunLevel;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxRunLevel(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxRunLevel(e.getMessage());
		}
	}

	/**
	 * Get SeLinux.
	 * 
	 * create on 2015-04-28
	 */
	public void tourSeLinux() {
		try {
			Command query = linuxQueryList.querySeLinux;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setSeLinux(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setSeLinux(e.getMessage());
		}
	}

	/**
	 * Get Storage.
	 * 
	 * create on 2015-05-25
	 */
	public void tourLinuxStorage() {
		try {
			Command query = linuxQueryList.queryStorage;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxStorage(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxStorage(e.getMessage());
		}
	}

	/**
	 * Get Swap Size.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxSwapSize() {
		try {
			Command query = linuxQueryList.querySwapSize;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxSwapSize(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxSwapSize(e.getMessage());
		}
	}

	/**
	 * Get /etc/sysconfig/network
	 * 
	 * create on 2015-04-27
	 */
	public void tourLinuxSysconfige() {
		try {
			Command query = linuxQueryList.querySysconfig;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxSysconfig(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxSysconfig(e.getMessage());
		}
	}

	/**
	 * Get Time Zone.
	 * 
	 * create on 2015-02-01
	 */
	public void tourLinuxTimeZone() {
		try {
			Command query = linuxQueryList.queryTimeZone;
			query.execute();
			String res = query.getResponse();
			this.realServerForLinux.setLinuxTimeZone(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.realServerForLinux.setLinuxTimeZone(e.getMessage());
		}
	}

	public void exitLinuxSever() throws Exception {

		if (sshc != null) {
			sshc.close();
		}
	}

	// this method must be used under a windows which performance option of
	// visual effects is whole turned off.
	public boolean connectToWinServer(String localosname, String destServerIP, String destServerUserName,
			String destServerPassword, String psosname) throws Exception {
		boolean res = false;
		wa = new WinAuto("");

		if (isFirstConnected == false) {
			this.realServerForWin = null;
			this.realServerForWin = new RealWinServerInfoBean();
			// mockWinServer = null;
		} else {
			this.realServerForWin = new RealWinServerInfoBean();
		}

		Thread.sleep(100);
		if (wa.logonWindServerFromLocal(localosname, destServerIP, destServerUserName, destServerPassword, psosname)) {
			res = true;
		} else {
			res = false;
			throw new Exception("Failed to logon provisioned server:" + destServerIP);
		}

		return res;
	}

	public boolean connectToWinServerThroughWinJumpStation(String localosname, String jumpstationip,
			String jumpstationusername, String jumpstationpassword, String jsosname, String destServerIP,
			String destServerUserName, String destServerPassword, String psosname) throws Exception {
		boolean res = false;
		wa = new WinAuto("");

		if (isFirstConnected == false) {
			this.realServerForWin = null;
			this.realServerForWin = new RealWinServerInfoBean();
		} else {
			this.realServerForWin = new RealWinServerInfoBean();
		}

		if (wa.logonWindServerThroughJumpStation(localosname, jumpstationip, jumpstationusername, jumpstationpassword,
				jsosname, destServerIP, destServerUserName, destServerPassword, psosname)) {

			res = true;
		} else {
			res = false;
			throw new Exception("Failed to logon provisioned server:" + destServerIP);
		}

		return res;
	}

	public void logoffWindSever(String ip) throws InterruptedException {
		wa.logoffServer(ip);
	}

	public void tourWindAllInfo() throws InterruptedException {
		String res = "";
		res = wa.queryAllInfoOfWindows();

		if (!res.equals("")) {
			NodeList nodes = null;
			try {

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/hostname");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setHostName(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/timezone/standardname");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setTimeZone(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/bios/name");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setBios(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/bios/version");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setBiosVer(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/memorychips//chip/capacity");
				if (nodes.getLength() > 0) {
					long size = 0;
					for (int i = 0; i < nodes.getLength(); i++) {
						String curStr = nodes.item(i).getTextContent().trim();
						if (curStr.trim().equals("")) {

						} else {
							size = size + Long.parseLong(curStr);
						}

					}
					this.realServerForWin.setMemorySize(String.valueOf(size) + "Byte");
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/videocontrollers//videocontroller/name");
				if (nodes.getLength() > 0) {
					ArrayList<String> names = new ArrayList<String>();
					for (int i = 0; i < nodes.getLength(); i++) {
						names.add(nodes.item(i).getTextContent().trim());
					}
					this.realServerForWin.setGraphicsName(names);
				}

				Thread.sleep(20);
				// cpu status is cpu number
				nodes = Utility.queryXml(res, "/info/cpu/cpustatus");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setCpustatus(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				NodeList mainnodes = Utility.queryXml(res, "/info/networkconfigs//config");

				Thread.sleep(20);
				NodeList subnodes1 = Utility.queryXml(res, "/info/networkconfigs//config/defaultipgateway");

				Thread.sleep(20);
				NodeList subnodes2 = Utility.queryXml(res, "/info/networkconfigs//config/description");

				Thread.sleep(20);
				NodeList subnodes3 = Utility.queryXml(res, "/info/networkconfigs//config/dnsdomain");

				Thread.sleep(20);
				NodeList subnodes4 = Utility.queryXml(res, "/info/networkconfigs//config/dnsdomainsuffixsearchorder");

				Thread.sleep(20);
				NodeList subnodes5 = Utility.queryXml(res, "/info/networkconfigs//config/dnshostname");

				Thread.sleep(20);
				NodeList subnodes6 = Utility.queryXml(res, "/info/networkconfigs//config/ipaddress");

				Thread.sleep(20);
				NodeList subnodes7 = Utility.queryXml(res, "/info/networkconfigs//config/macaddress");
				if (mainnodes.getLength() > 0) {
					ArrayList<HashMap<String, String>> configs = new ArrayList<HashMap<String, String>>();

					for (int i = 0; i < mainnodes.getLength(); i++) {
						String curStr1 = subnodes1.item(i).getTextContent().trim();
						String curStr2 = subnodes2.item(i).getTextContent().trim();
						String curStr3 = subnodes3.item(i).getTextContent().trim();
						String curStr4 = subnodes4.item(i).getTextContent().trim();
						String curStr5 = subnodes5.item(i).getTextContent().trim();
						String curStr6 = subnodes6.item(i).getTextContent().trim();
						String curStr7 = subnodes7.item(i).getTextContent().trim();
						String ipv4 = "";
						String ipv6 = "";
						if (!curStr6.trim().equals("")) {
							String[] ips = curStr6.split("\",");
							ipv4 = ips[0].substring(2);
							if (ips.length > 1) {
								ipv6 = ips[1].replaceAll("\"", "");
							}
						}
						HashMap<String, String> line = new HashMap<String, String>();
						line.put("defaultipgateway", curStr1);
						line.put("description", curStr2);
						line.put("dnsdomain", curStr3);
						line.put("dnsdomainsuffixsearchorder", curStr4);
						line.put("dnshostname", curStr5);
						line.put("ipv4", ipv4);
						line.put("ipv6", ipv6);
						line.put("macaddress", curStr7);
						configs.add(line);

					}

					this.realServerForWin.setNetworkConfig(configs);
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/os/name");
				if (nodes.getLength() > 0) {
					String fullosname = nodes.item(0).getTextContent().trim();
					String osname = fullosname.split("\\|")[0].trim();
					this.realServerForWin.setOsName(osname);
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/os/osarchitecture");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setOsArch(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/os/version");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setOsVersion(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/ntp");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setNtp(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				NodeList nodes4 = Utility.queryXml(res, "/info/logicaldisks//drive/size");

				Thread.sleep(20);
				NodeList nodes5 = Utility.queryXml(res, "/info/logicaldisks//drive/name");
				if (nodes4.getLength() > 0) {
					ArrayList<String[]> lines = new ArrayList<String[]>();
					for (int i = 0; i < nodes4.getLength(); i++) {
						String curSize = nodes4.item(i).getTextContent().trim();
						String curName = nodes5.item(i).getTextContent().trim();
						if (curSize.trim().equals("")) {
							String[] line = { curName, curSize };
							lines.add(line);
						} else {
							String[] line = { curName, curSize };
							lines.add(line);
						}

					}
					this.realServerForWin.setDiskSize(lines);
				}

				Thread.sleep(20);
				NodeList nodes6 = Utility.queryXml(res, "/info/physicaldisks//device/size");

				Thread.sleep(20);
				NodeList nodes7 = Utility.queryXml(res, "/info/physicaldisks//device/name");
				if (nodes7.getLength() > 0) {
					ArrayList<String[]> lines = new ArrayList<String[]>();
					for (int i = 0; i < nodes7.getLength(); i++) {
						String curSize = nodes6.item(i).getTextContent().trim();
						String curName = nodes6.item(i).getTextContent().trim();
						if (curSize.trim().equals("")) {
							String[] line = { curName, curSize };
							lines.add(line);
						} else {
							String[] line = { curName, curSize };
							lines.add(line);
						}

					}
					this.realServerForWin.setDiskDeviceSize(lines);
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/localhost");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setLocalHosts(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				NodeList nodes1 = Utility.queryXml(res, "/info/firewall/domainprofile/state");

				Thread.sleep(20);
				NodeList nodes2 = Utility.queryXml(res, "/info/firewall/privateprofile/state");

				Thread.sleep(20);
				NodeList nodes3 = Utility.queryXml(res, "/info/firewall/puplicprofile/state");
				if (nodes1.getLength() >= 0 & nodes2.getLength() >= 0 & nodes3.getLength() >= 0) {
					if (nodes1.item(0).getTextContent().trim().toLowerCase().equals("off")
							& nodes2.item(0).getTextContent().trim().toLowerCase().equals("off")
							& nodes3.item(0).getTextContent().trim().toLowerCase().equals("off")) {
						this.realServerForWin.setIsfirewallOn(false);
					} else {
						this.realServerForWin.setIsfirewallOn(true);
					}
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/uac/level");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setUACLevel(nodes.item(0).getTextContent().trim());
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res,
						"/info/servermanagerconfiguration//Feature/DisplayName[@DisplayName='Multipath I/O'][@Installed='true']");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setIsMultipathIOInstalled(true);
				} else {
					this.realServerForWin.setIsMultipathIOInstalled(false);
				}

				Thread.sleep(20);
				NodeList nodesname = Utility.queryXml(res, "/info/products//product/name");
				NodeList nodesinstallstate = Utility.queryXml(res, "/info/products//product/installstate");
				if (nodesname.getLength() > 0) {
					for (int i = 0; i < nodesname.getLength(); i++) {
						if (nodesname.item(i).getTextContent().trim().toLowerCase()
								.equals("HP MPIO DSM Manager".toLowerCase())) {
							if (nodesinstallstate.item(i).getTextContent().trim().equals("5")) {
								this.realServerForWin.setIsMPIODSMManagerInstalled(true);
							} else {
								this.realServerForWin.setIsMPIODSMManagerInstalled(false);
							}
							break;
						}
					}

				} else {
					this.realServerForWin.setIsMPIODSMManagerInstalled(false);
				}

				Thread.sleep(20);
				nodes = Utility.queryXml(res, "/info/MPIOConfiguraion");
				if (nodes.getLength() > 0) {
					this.realServerForWin.setMPIOConfig(nodes.item(0).getTextContent().trim());
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
