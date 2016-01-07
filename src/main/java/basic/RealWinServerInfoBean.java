
package basic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * an bean for store the information of a windows server that can be used by
 * {@link utilities.RealServerInspector}
 * 
 * @author zhangyutao
 *
 */
public class RealWinServerInfoBean {
	private String hostName;
	private String timeZone;
	private String bios;
	private String biosVer;
	private String memorySize;
	private ArrayList<String> graphicsName;
	private String cpustatus;
	private ArrayList<HashMap<String, String>> networkConfig;// lines:HashMap<name,value>
	private String osName;
	private String osVersion;
	private String ntp;
	private ArrayList<String[]> diskSize;// lines:{name, size}
	private ArrayList<String[]> diskDeviceSize;// lines:{name, size}
	private String localHosts;
	private boolean isfirewallOn;
	private String UACLevel;
	private boolean IsMultipathIOInstalled;// is Multipath I/O feature installed
	private boolean IsMPIODSMManagerInstalled; // is HP MPIO DSM Manager
												// installed
	private String MPIOConfig;
	private String osArch;

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public RealWinServerInfoBean() {

	}

	public boolean isIsfirewallOn() {
		return isfirewallOn;
	}

	public void setIsfirewallOn(boolean isfirewallOn) {
		this.isfirewallOn = isfirewallOn;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getBios() {
		return bios;
	}

	public void setBios(String bios) {
		this.bios = bios;
	}

	public String getBiosVer() {
		return biosVer;
	}

	public void setBiosVer(String biosVer) {
		this.biosVer = biosVer;
	}

	public String getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}

	public ArrayList<String> getGraphicsName() {
		return graphicsName;
	}

	public void setGraphicsName(ArrayList<String> graphicsName) {
		this.graphicsName = graphicsName;
	}

	public String getCpustatus() {
		return cpustatus;
	}

	public void setCpustatus(String cpustatus) {
		this.cpustatus = cpustatus;
	}

	public ArrayList<HashMap<String, String>> getNetworkConfig() {
		return networkConfig;
	}

	public void setNetworkConfig(ArrayList<HashMap<String, String>> networkConfig) {
		this.networkConfig = networkConfig;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getNtp() {
		return ntp;
	}

	public void setNtp(String ntp) {
		this.ntp = ntp;
	}

	public ArrayList<String[]> getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(ArrayList<String[]> diskSize) {
		this.diskSize = diskSize;
	}

	public ArrayList<String[]> getDiskDeviceSize() {
		return diskDeviceSize;
	}

	public void setDiskDeviceSize(ArrayList<String[]> diskDeviceSize) {
		this.diskDeviceSize = diskDeviceSize;
	}

	public String getLocalHosts() {
		return localHosts;
	}

	public void setLocalHosts(String localHosts) {
		this.localHosts = localHosts;
	}

	public String getUACLevel() {
		return UACLevel;
	}

	public void setUACLevel(String uACLevel) {
		UACLevel = uACLevel;
	}

	public boolean isIsMultipathIOInstalled() {
		return IsMultipathIOInstalled;
	}

	public void setIsMultipathIOInstalled(boolean isMultipathIOInstalled) {
		IsMultipathIOInstalled = isMultipathIOInstalled;
	}

	public boolean isIsMPIODSMManagerInstalled() {
		return IsMPIODSMManagerInstalled;
	}

	public void setIsMPIODSMManagerInstalled(boolean isMPIODSMManagerInstalled) {
		IsMPIODSMManagerInstalled = isMPIODSMManagerInstalled;
	}

	public String getMPIOConfig() {
		return MPIOConfig;
	}

	public void setMPIOConfig(String mPIOConfig) {
		MPIOConfig = mPIOConfig;
	}

}
