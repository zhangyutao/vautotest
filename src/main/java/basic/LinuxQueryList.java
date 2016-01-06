package basic;

import annotations.cmd.Line;
import elements.Command;

public class LinuxQueryList {
	public static final String deafultIP = "0.0.0.0";

	@Line("nslookup " + deafultIP + " | grep \"name\"")
	public Command queryDNS;

	@Line("getconf LONG_BIT")
	public Command queryBit;

	@Line("cat /proc/cpuinfo | grep \"processor\" | wc -l")
	public Command queryCPU;

	@Line("if [ `ifconfig | grep -n \"eth1\" | wc -l` -ne 0 ]; then ifconfig | "
			+ "sed -n \"`expr $(ifconfig | grep -n \"eth1\" | cut -d \":\" -f 1) + 1`p\" | "
			+ "awk -F: '{print $2}' | awk '{print $1}'; else echo \"\"; fi")
	public Command queryFloatingIP;

	@Line("if [ `ifconfig | grep -n \"eth1\" | wc -l` -ne 0 ]; "
			+ "then ifconfig | sed -n \"`expr $(ifconfig | grep -n \"eth0\" "
			+ "| cut -d \":\" -f 1)`p\" | awk '{print $NF}'; else echo \"\"; fi")
	public Command queryFloatingMac;

	@Line("hostname")
	public Command queryHostname;

	@Line("cat /etc/hosts")
	public Command queryHosts;

	@Line("value=0; for loop in `dmidecode -t 17 | grep \"Size\" | grep -v \"No Module Installed\" "
			+ "| sed \"s/[^0-9]*//g\"`; do value=`expr $value + $loop`; done; echo $value\"MB\"")
	public Command queryMemory;

	@Line("/etc/init.d/ntpd start; ntpq -p")
	public Command queryNTP;

	@Line("lsb_release -a | grep \"^Description:\" | awk -F: '{print $2}' | sed -n '1~2{s/[( |\t)]//;p}'")
	public Command queryOSVersion;

	@Line("if [ `ifconfig | grep -n \"bond0\" | wc -l` -ne 0 ]; "
			+ "then ifconfig | sed -n \"`expr $(ifconfig | grep -n \"bond0\" | cut -d \":\" -f 1) + 1`p\" | awk -F: '{print $2}' | awk '{print $1}'; "
			+ "elif [ `ifconfig | grep -n \"eth0\" | wc -l` -ne 0 ]; "
			+ "then ifconfig | sed -n \"`expr $(ifconfig | grep -n \"eth0\" | cut -d \":\" -f 1) + 1`p\" | awk -F: '{print $2}' | awk '{print $1}'; "
			+ "else echo \"\"; fi")
	public Command queryPrivateIP;

	@Line("if [ `ifconfig | grep -n \"bond0\" | wc -l` -ne 0 ]; "
			+ "then ifconfig | sed -n \"`expr $(ifconfig | grep -n \"bond0\" | cut -d \":\" -f 1)`p\" | awk '{print $NF}'; "
			+ "elif [ `ifconfig | grep -n \"eth0\" | wc -l` -ne 0 ]; "
			+ "then ifconfig | sed -n \"`expr $(ifconfig | grep -n \"eth0\" | cut -d \":\" -f 1)`p\" | awk '{print $NF}'; "
			+ "else echo \"\"; fi")
	public Command queryPrivateMac;

	@Line("cat /proc/meminfo | grep \"MemTotal\" | awk -F: '{print $2}' | tr -d \" \"")
	public Command queryRAM;

	@Line("runlevel")
	public Command queryRunLevel;

	@Line("[ -r /etc/selinux/config ]; if [ $? -eq 0 ]; then sestatus; else echo \"\"; fi")
	public Command querySeLinux;

	@Line("[ -r /etc/multipath.conf ]; if [ $? -eq 0 ]; "
			+ "then echo \"physical\"; multipath -ll | egrep \"mpath|^size\" | sed 'N;s/\\n/;/g'; "
			+ "else echo \"virtual\"; fdisk -l | grep \"dev\" | grep -v \"/dev/mapper\" | grep -v \"/dev/sda\"; fi")
	public Command queryStorage;

	@Line("echo `free -g | tail -1 | awk '{print $2}'`\"GB\"")
	public Command querySwapSize;

	@Line("[ -r /etc/sysconfig/network ]; if [ $? -eq 0 ]; then cat /etc/sysconfig/network; else echo \"\"; fi")
	public Command querySysconfig;

	@Line("date \"+%Z\"")
	public Command queryTimeZone;

}
