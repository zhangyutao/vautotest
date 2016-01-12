package basic.windows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.sikuli.natives.FindResult;
import org.sikuli.script.*;
import basic.Config;
import utilities.Utility;

/**
 * this class is under developping...be careful to use it.
 * 
 * @author zhangyutao
 *
 */
public class WinRDAuto {

	private String firstServerIP = "";
	private String allinfobatOfWind = "";
	private String styleswitchPath = Config.RESOURCEPATH + "/styleswitch/";
	private String copyfileplugfolderpath = Config.RESOURCEPATH + "/copyfileplug";
	private String copyfileplugname = "clipplug.exe";
	private String copyfileplugpath = copyfileplugfolderpath + "/" + copyfileplugname;

	private String remotewintowinfulpath = "";
	private String tempfilesfolder = "tempfiles";
	private String tempfilesPath = tempfilesfolder + "/";
	private String desktoppath = "%userprofile%\\Desktop\\";
	private String runselfasadmin = "@echo on" + "\n" + "@echo Run as Admin" + "\n" + "@echo off" + "\n" + "%1 %2"
			+ "\n" + "ver|find \"5.\">nul&&goto :st" + "\n"
			+ "mshta vbscript:createobject(\"shell.application\").shellexecute(\"%~s0\",\"goto :st\",\"\",\"runas\",1)(window.close)&goto :eof"
			+ "\n" + ":st" + "\n";

	private String rtsbatname = "rtsbat.bat";
	private String rtsbatLocalpath = tempfilesPath + rtsbatname;
	private String defaultTempInfo = "%userprofile%\\Desktop\\tempinfo.xml";
	private String batOutputFileContentTermination = "@fe0";
	private String tellClipOutputFilePath = "cpc@";
	private String tellClipFileExist = "@cd";
	private String tellClipNoError = "@e0";
	private String tellClipError2 = "@e2";

	public WinRDAuto(String pathOfTempfiles) throws Exception {
		try {
			String osname = System.getProperty("os.name");
			String styleswitchAbaPath = (new File(styleswitchPath)).getAbsolutePath();

			String cmdChangeTheme = "";
			String cmdChangeFontSmoothing = "\"" + styleswitchAbaPath + "\\ClearTypeSwitch.exe\" s- c-";

			if (osname.contains("2008") || osname.contains("7")) {
				cmdChangeTheme = "\"" + styleswitchAbaPath + "\\Windows 7\\themeswitcher.exe\"" + " \""
						+ styleswitchAbaPath + "\\Windows 7\\win7.theme\"";
				Utility.runProcess(cmdChangeTheme, 30);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Utility.runProcess(cmdChangeFontSmoothing, 30);
			}

			if (osname.contains("2012") || (osname.contains("8") && !osname.contains("2008"))) {
				cmdChangeTheme = "\"" + styleswitchAbaPath + "\\Windows 8\\themeswitcher.exe\"" + " \""
						+ styleswitchAbaPath + "\\Windows 8\\win8.theme\"";
				Utility.runProcess(cmdChangeTheme, 30);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Utility.runProcess(cmdChangeFontSmoothing, 30);
			}

		} catch (Exception e) {
			throw new Exception("Cannot set desktop for automation: " + e.getMessage());
		}

		int askRunAsAdmin = Integer.parseInt(Config.ASKRUNADMIN);
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			firstServerIP = addr.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (askRunAsAdmin == 1) {
			runselfasadmin = "@echo on" + "\n" + "@echo Run as Admin" + "\n" + "@echo off" + "\n" + "%1 %2" + "\n"
					+ "ver|find \"5.\">nul&&goto :st" + "\n"
					+ "mshta vbscript:createobject(\"shell.application\").shellexecute(\"%~s0\",\"goto :st\",\"\",\"runas\",1)(window.close)&goto :eof"
					+ "\n" + ":st" + "\n";
		} else {
			runselfasadmin = "";
		}

		if (!pathOfTempfiles.equals("")) {
			tempfilesPath = pathOfTempfiles + "/" + tempfilesfolder + "/";
		}

		org.sikuli.basics.Settings.OcrTextRead = true;
		Location loc = new Location(0, 0);
		MouseAdv.move(loc);
	}

	/**
	 * logon a Remote Desktop
	 * 
	 * @param destServerIP
	 * @param destServerUser
	 * @param destServerPWD
	 * @return
	 * @throws InterruptedException
	 */
	public boolean logonRemoteDesktop(String destServerIP, String destServerUser, String destServerPWD)
			throws InterruptedException {

		return logonRemoteDesktop(WinType.win2008, destServerIP, destServerUser, destServerPWD, WinType.win2008);
	}

	/**
	 * logon a Remote Desktop
	 * 
	 * @param localWT
	 * @param destServerIP
	 * @param destServerUser
	 * @param destServerPWD
	 * @param destServerWT
	 * @return
	 * @throws InterruptedException
	 */
	public boolean logonRemoteDesktop(WinType localWT, String destServerIP, String destServerUser, String destServerPWD,
			WinType destServerWT) throws InterruptedException {

		boolean res = false;
		minAllWindows();

		Thread.sleep(100);
		if (!localToRemoteServer(localWT, destServerIP, destServerUser, destServerPWD, destServerWT)) {
			res = false;
			throw new IllegalArgumentException("failed to log on Remote Desktop: " + destServerUser);

		} else {
			res = true;
			System.out.println("successful to log on Remote Desktop: " + destServerIP);
		}
		return res;
	}

	// the user of js and destServer must be admin which can run cmd as
	// admin directly.
	/**
	 * logon a Remote Desktop through a windows jump station's Remote Desktop
	 * 
	 * @param jsIP
	 * @param jsUser
	 * @param jsPassword
	 * @param destServerIP
	 * @param destServerUser
	 * @param destServerPWD
	 * @return
	 * @throws InterruptedException
	 */
	public boolean logonRemoteDesktop(String jsIP, String jsUser, String jsPassword, String destServerIP,
			String destServerUser, String destServerPWD) throws InterruptedException {

		return logonRemoteDesktop(WinType.win2008, jsIP, jsUser, jsPassword, WinType.win2008, destServerIP,
				destServerUser, destServerPWD, WinType.win2008);
	}

	// the user of js and destServer must be admin which can run cmd as
	// admin directly.
	/**
	 * logon a Remote Desktop through a windows jump station's Remote Desktop
	 * 
	 * @param localWT
	 * @param jsIP
	 * @param jsUser
	 * @param jsPassword
	 * @param jsosname
	 * @param destServerIP
	 * @param destServerUser
	 * @param destServerPWD
	 * @param destServerWT
	 * @return
	 * @throws InterruptedException
	 */
	public boolean logonRemoteDesktop(WinType localWT, String jsIP, String jsUser, String jsPassword, WinType jumpWT,
			String destServerIP, String destServerUser, String destServerPWD, WinType destServerWT)
					throws InterruptedException {

		boolean res = false;
		minAllWindows();

		if (!localToRemoteServer(localWT, jsIP, jsUser, jsPassword, jumpWT)) {
			res = false;
			throw new IllegalArgumentException("failed to log on Jump Station: " + jsIP);

		} else {
			minAllWindows();
			if (!remoteToServer(jumpWT, destServerIP, destServerUser, destServerPWD, destServerWT)) {
				res = false;
				throw new IllegalArgumentException("failed to log on Remote Desktop: " + destServerIP);

			} else {
				res = true;
				System.out.println("successful to log on Remote Desktop: " + destServerIP);
			}
		}
		return res;
	}

	/**
	 * log off Remote Desktop
	 * 
	 * @param ip
	 * @return
	 * @throws InterruptedException
	 */
	public boolean logoffRemoteDesktop(String ip) throws InterruptedException {
		System.out.println("Preparing to logoff Remote Desktop:" + ip);

		Thread.sleep(10000);

		boolean res = false;
		Screen scr = new Screen();
		if (waitRemoteServerShowed(scr, ip)) {
			String logoffcmd = "logoff";
			if (!winRunByUsingClip(logoffcmd)) {
				throw new IllegalArgumentException("Failed to logoff server:" + ip);
			} else {
				res = true;
				System.out.println("Successful to logoff server:" + ip);
			}
		} else {
			throw new IllegalArgumentException("Current server screen is not you wanted:" + ip);
		}
		return res;
	}

	/**
	 * use a .bat file to query. if you have your self query, please edit it in
	 * the .bat file which path is @see #allinfobatOfWind
	 * 
	 * @return - all the information of .txt file which is generated by the
	 *         .bat.
	 * @throws InterruptedException
	 */
	public String queryAllInfo() throws InterruptedException {
		File cmdBat = new File(allinfobatOfWind);
		String res = "";
		String content = "";
		try {
			content = FileUtils.readFileToString(cmdBat);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!content.equals("")) {
			res = runBATThroughWinRun(content, defaultTempInfo);
		}
		return res;
	}

	/**
	 * use a user-defined WinBat class to query. about how to create WinBat
	 * class, please see {@link #WinBat}.
	 * 
	 * @param bat
	 * @return - all the information of .txt file which is generated by the
	 *         .bat.
	 * @throws InterruptedException
	 */
	public String queryInfo(WinBat bat) throws InterruptedException {

		String res = "";
		String content = "";
		try {
			content = FileUtils.readFileToString(bat.getBatFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!content.equals("")) {
			res = runBATThroughWinRun(content, bat.getPathOfOutputFile());
		}
		return res;
	}

	private void chooseWindAutomationSolution(WinType wt) {
		/*
		 * Screen scr = new Screen();
		 *
		 * Rectangle screenRectangle = scr.getBounds(); if
		 * (screenRectangle.width * screenRectangle.height == 2073600) {
		 * sikulixResolution = "1920x1080"; } sikulixResolution = "1920x1080";
		 */
		if (wt == WinType.win2008) {
			allinfobatOfWind = WinBatTemplate.win2008.getPath();
			remotewintowinfulpath = WinResolution.W2008R1920X1080.getPath();
		} else if (wt == WinType.win2012) {
			allinfobatOfWind = WinBatTemplate.win2012.getPath();
			remotewintowinfulpath = WinResolution.W2012R1920X1080.getPath();
		} else {
			throw new IllegalArgumentException("Error parameter");
		}
	}

	private String ltsbat(String backendIP, String username, String pathOfBat) {
		String res = "";
		Screen cscreen = new Screen(0);
		String rdppath = "%userprofile%\\Desktop\\Temp.rdp";
		String waitW = "6";
		String waitE = "2";
		String waitD = "6";
		try {
			File f = new File(pathOfBat);
			if (!f.exists()) {
				Utility.createFile(f);
				f.getAbsolutePath();
			} else {
				f.delete();
				Thread.sleep(2000);
				Utility.createFile(f);
			}
			f.setReadable(true);
			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);
			pw.write("");

			pw.append("@echo on | clip" + "\n" + "@set rdp=" + rdppath + "\n" + "@echo Create RDP:%rdp%" + "\n"
					+ "@echo off" + "\n" + "echo screen mode id:i:2>%rdp%" + "\n" + "echo use multimon:i:0>>%rdp%"
					+ "\n" + "echo desktopwidth:i:" + String.valueOf(cscreen.w) + ">>%rdp%" + "\n"
					+ "echo desktopheight:i:" + String.valueOf(cscreen.h) + ">>%rdp%" + "\n"
					+ "echo session bpp:i:32>>%rdp%" + "\n" + "echo winposstr:s:0,3,0,0,800,600>>%rdp%" + "\n"
					+ "echo compression:i:1>>%rdp%" + "\n" + "echo keyboardhook:i:2>>%rdp%" + "\n"
					+ "echo audiocapturemode:i:0>>%rdp%" + "\n" + "echo videoplaybackmode:i:1>>%rdp%" + "\n"
					+ "echo connection type:i:1>>%rdp%" + "\n" + "echo displayconnectionbar:i:1>>%rdp%" + "\n"
					+ "echo disable wallpaper:i:1>>%rdp%" + "\n" + "echo allow font smoothing:i:0>>%rdp%" + "\n"
					+ "echo allow desktop composition:i:0>>%rdp%" + "\n" + "echo disable full window drag:i:1>>%rdp%"
					+ "\n" + "echo disable menu anims:i:1>>%rdp%" + "\n" + "echo disable themes:i:1>>%rdp%" + "\n"
					+ "echo disable cursor setting:i:0>>%rdp%" + "\n" + "echo bitmapcachepersistenable:i:1>>%rdp%"
					+ "\n" + "echo full address:s:" + backendIP + ">>%rdp%" + "\n" + "echo audiomode:i:0>>%rdp%" + "\n"
					+ "echo redirectprinters:i:1>>%rdp%" + "\n" + "echo redirectcomports:i:0>>%rdp%" + "\n"
					+ "echo redirectsmartcards:i:1>>%rdp%" + "\n" + "echo redirectclipboard:i:1>>%rdp%" + "\n"
					+ "echo redirectposdevices:i:0>>%rdp%" + "\n" + "echo redirectdirectx:i:1>>%rdp%" + "\n"
					+ "echo autoreconnection enabled:i:1>>%rdp%" + "\n" + "echo authentication level:i:0>>%rdp%" + "\n"
					+ "echo prompt for credentials:i:0>>%rdp%" + "\n" + "echo negotiate security layer:i:1>>%rdp%"
					+ "\n" + "echo remoteapplicationmode:i:0>>%rdp%" + "\n"
					+ "echo alternate shell:s:rdpclip.exe>>%rdp%" + "\n" + "echo shell working directory:s:>>%rdp%"
					+ "\n" + "echo gatewayhostname:s:>>%rdp%" + "\n" + "echo gatewayusagemethod:i:4>>%rdp%" + "\n"
					+ "echo gatewaycredentialssource:i:4>>%rdp%" + "\n" + "echo gatewayprofileusagemethod:i:0>>%rdp%"
					+ "\n" + "echo promptcredentialonce:i:1>>%rdp%" + "\n"
					+ "echo use redirection server name:i:0>>%rdp%" + "\n" + "echo drivestoredirect:s:>>%rdp%" + "\n"
					+ "echo username:s:" + username + ">>%rdp%" + "\n" + "if %ERRORLEVEL% GTR 0 goto RDPWError" + "\n"
					+ "if %ERRORLEVEL%== 0 goto OpenRDP" + "\n" + ":OpenRDP" + "\n" + "ping -n " + waitW
					+ " 127.0.0.1>nul" + "\n" + "if %ERRORLEVEL% GTR 0 goto PingError" + "\n" + "set findtask=0" + "\n"
					+ "TASKLIST /FI \"IMAGENAME eq mstsc*\"|findstr \"mstsc\" > nul && (set findtask=1) || (set findtask=0)"
					+ "\n" + "if %findtask%==1 (TASKKILL /FI \"IMAGENAME eq mstsc*\" /F)" + "\n" + "set a=0" + "\n"
					+ ":Loop" + "\n" + "set /a a+=1" + "\n"
					+ "if exist \"%rdp%\" (goto RunRDP) else (if %a% == 30 (goto RDPRError) else (goto Wait)) " + "\n"
					+ ":Wait" + "\n" + "ping -n " + waitE + " 127.0.0.1>nul" + "\n"
					+ "if %ERRORLEVEL% GTR 0 goto PingError" + "\n" + "goto Loop" + "\n" + ":RunRDP" + "\n"
					+ "ping -n 6 127.0.0.1>nul" + "\n" + "@echo on " + "\n" + "@echo Run RDP:%rdp%" + "\n" + "@echo off"
					+ "\n" + "Start \"\" \"%rdp%\"" + "\n" + "if %ERRORLEVEL% GTR 0 goto RDPRError" + "\n"
					+ "if %ERRORLEVEL% == 0 goto Success" + "\n" + ":Success" + "\n" + "ping -n " + waitD
					+ " 127.0.0.1>nul" + "\n" + "if %ERRORLEVEL% GTR 0 goto PingError" + "\n" + "@echo Delete RDP:%rdp%"
					+ "\n" + "@echo off" + "\n" + "del \"%rdp%\"" + "\n" + "echo " + tellClipNoError + "| clip" + "\n"
					+ "goto End" + "\n" + ":RDPWError" + "\n" + "echo failed to write rdp" + tellClipError2 + "| clip"
					+ "\n" + "goto End" + "\n" + ":RDPRError" + "\n" + "echo failed to run rdp" + tellClipError2
					+ "| clip" + "\n" + "goto End" + "\n" + ":PingError" + "\n" + "echo pingerror" + tellClipError2
					+ "| clip" + "\n" + "goto End" + "\n" + "goto End" + "\n" + ":End" + "\n" + "del %0");

			pw.flush();
			pw.close();
			fw.close();
			f = null;
			String batfullpath = (new File(pathOfBat)).getAbsolutePath();
			res = batfullpath;

			Calendar calendar = Calendar.getInstance();
			Date current = new Date();
			calendar.setTime(current);
			calendar.add(Calendar.SECOND, 10);
			Date breakTime = calendar.getTime();
			File fnew = new File(batfullpath);

			while (!fnew.exists()) {

				Thread.sleep(1000);
				fnew = new File(batfullpath);
				current = new Date();
				if (current.after(breakTime)) {
					res = "";
					throw new IllegalArgumentException("failed to generate .bat: " + batfullpath);

				}
			}

			System.out.println("successful to generate .bat: " + batfullpath);
			cscreen = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	// generate BAT file for Write, run and delete Temp.rdp.
	private String rtsbat(String backendIP, String username, String pathOfBat) {
		String res = "";
		Screen cscreen = new Screen(0);
		String rdppath = "%userprofile%\\Desktop\\Temp.rdp";
		String waitW = "6";
		String waitE = "2";
		String waitD = "6";
		try {
			File f = new File(pathOfBat);
			if (!f.exists()) {
				Utility.createFile(f);
				f.getAbsolutePath();
			} else {
				f.delete();
				Thread.sleep(2000);
				Utility.createFile(f);
			}
			f.setReadable(true);
			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);
			pw.write("");

			pw.append(runselfasadmin + "@echo on | clip" + "\n" + "@set rdp=" + rdppath + "\n"
					+ "@echo Create RDP:%rdp%" + "\n" + "@echo off" + "\n" + "echo screen mode id:i:2>%rdp%" + "\n"
					+ "echo use multimon:i:0>>%rdp%" + "\n" + "echo desktopwidth:i:" + String.valueOf(cscreen.w)
					+ ">>%rdp%" + "\n" + "echo desktopheight:i:" + String.valueOf(cscreen.h) + ">>%rdp%" + "\n"
					+ "echo session bpp:i:32>>%rdp%" + "\n" + "echo winposstr:s:0,3,0,0,800,600>>%rdp%" + "\n"
					+ "echo compression:i:1>>%rdp%" + "\n" + "echo keyboardhook:i:2>>%rdp%" + "\n"
					+ "echo audiocapturemode:i:0>>%rdp%" + "\n" + "echo videoplaybackmode:i:1>>%rdp%" + "\n"
					+ "echo connection type:i:1>>%rdp%" + "\n" + "echo displayconnectionbar:i:1>>%rdp%" + "\n"
					+ "echo disable wallpaper:i:1>>%rdp%" + "\n" + "echo allow font smoothing:i:0>>%rdp%" + "\n"
					+ "echo allow desktop composition:i:0>>%rdp%" + "\n" + "echo disable full window drag:i:1>>%rdp%"
					+ "\n" + "echo disable menu anims:i:1>>%rdp%" + "\n" + "echo disable themes:i:1>>%rdp%" + "\n"
					+ "echo disable cursor setting:i:0>>%rdp%" + "\n" + "echo bitmapcachepersistenable:i:1>>%rdp%"
					+ "\n" + "echo full address:s:" + backendIP + ">>%rdp%" + "\n" + "echo audiomode:i:0>>%rdp%" + "\n"
					+ "echo redirectprinters:i:1>>%rdp%" + "\n" + "echo redirectcomports:i:0>>%rdp%" + "\n"
					+ "echo redirectsmartcards:i:1>>%rdp%" + "\n" + "echo redirectclipboard:i:1>>%rdp%" + "\n"
					+ "echo redirectposdevices:i:0>>%rdp%" + "\n" + "echo redirectdirectx:i:1>>%rdp%" + "\n"
					+ "echo autoreconnection enabled:i:1>>%rdp%" + "\n" + "echo authentication level:i:0>>%rdp%" + "\n"
					+ "echo prompt for credentials:i:0>>%rdp%" + "\n" + "echo negotiate security layer:i:1>>%rdp%"
					+ "\n" + "echo remoteapplicationmode:i:0>>%rdp%" + "\n"
					+ "echo alternate shell:s:rdpclip.exe>>%rdp%" + "\n" + "echo shell working directory:s:>>%rdp%"
					+ "\n" + "echo gatewayhostname:s:>>%rdp%" + "\n" + "echo gatewayusagemethod:i:4>>%rdp%" + "\n"
					+ "echo gatewaycredentialssource:i:4>>%rdp%" + "\n" + "echo gatewayprofileusagemethod:i:0>>%rdp%"
					+ "\n" + "echo promptcredentialonce:i:1>>%rdp%" + "\n"
					+ "echo use redirection server name:i:0>>%rdp%" + "\n" + "echo drivestoredirect:s:>>%rdp%" + "\n"
					+ "echo username:s:" + username + ">>%rdp%" + "\n" + "if %ERRORLEVEL% GTR 0 goto RDPWError" + "\n"
					+ "if %ERRORLEVEL%== 0 goto OpenRDP" + "\n" + ":OpenRDP" + "\n" + "ping -n " + waitW
					+ " 127.0.0.1>nul" + "\n" + "if %ERRORLEVEL% GTR 0 goto PingError" + "\n" + "set findtask=0" + "\n"
					+ "TASKLIST /FI \"IMAGENAME eq mstsc*\"|findstr \"mstsc\" > nul && (set findtask=1) || (set findtask=0)"
					+ "\n" + "if %findtask%==1 (TASKKILL /FI \"IMAGENAME eq mstsc*\" /F)" + "\n" + "set a=0" + "\n"
					+ ":Loop" + "\n" + "set /a a+=1" + "\n"
					+ "if exist \"%rdp%\" (goto RunRDP) else (if %a% == 30 (goto RDPRError) else (goto Wait)) " + "\n"
					+ ":Wait" + "\n" + "ping -n " + waitE + " 127.0.0.1>nul" + "\n"
					+ "if %ERRORLEVEL% GTR 0 goto PingError" + "\n" + "goto Loop" + "\n" + ":RunRDP" + "\n"
					+ "ping -n 6 127.0.0.1>nul" + "\n" + "@echo on " + "\n" + "@echo Run RDP:%rdp%" + "\n" + "@echo off"
					+ "\n" + "Start \"\" \"%rdp%\"" + "\n" + "if %ERRORLEVEL% GTR 0 goto RDPRError" + "\n"
					+ "if %ERRORLEVEL% == 0 goto Success" + "\n" + ":Success" + "\n" + "ping -n " + waitD
					+ " 127.0.0.1>nul" + "\n" + "if %ERRORLEVEL% GTR 0 goto PingError" + "\n" + "@echo Delete RDP:%rdp%"
					+ "\n" + "@echo off" + "\n" + "del \"%rdp%\"" + "\n" + "echo " + tellClipNoError + "| clip" + "\n"
					+ "goto End" + "\n" + ":RDPWError" + "\n" + "echo failed to write rdp" + tellClipError2 + "| clip"
					+ "\n" + "goto End" + "\n" + ":RDPRError" + "\n" + "echo failed to run rdp" + tellClipError2
					+ "| clip" + "\n" + "goto End" + "\n" + ":PingError" + "\n" + "echo pingerror" + tellClipError2
					+ "| clip" + "\n" + "goto End" + "\n" + "goto End" + "\n" + ":End" + "\n" + "del %0");

			pw.flush();
			pw.close();
			fw.close();
			f = null;
			String batfullpath = (new File(pathOfBat)).getAbsolutePath();
			res = batfullpath;

			Calendar calendar = Calendar.getInstance();
			Date current = new Date();
			calendar.setTime(current);
			calendar.add(Calendar.SECOND, 10);
			Date breakTime = calendar.getTime();
			File fnew = new File(batfullpath);

			while (!fnew.exists()) {

				Thread.sleep(1000);
				fnew = new File(batfullpath);
				current = new Date();
				if (current.after(breakTime)) {
					res = "";
					throw new IllegalArgumentException("failed to generate .bat: " + batfullpath);

				}
			}

			System.out.println("successful to generate .bat: " + batfullpath);
			cscreen = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * generate BAT file for Write, run and delete Temp.rdp.
	 * 
	 * @param Path
	 *            - where to create the file.
	 * @param pathOfOutputFile
	 *            - It must be absolute path. The path of file which is written
	 *            in first parameter "batFile" @see #batFile to get all output.
	 * @return the Absolute Path of the new bat file
	 */
	private String normalbat(String content, String Path, String pathOfOutputFile) {
		String outputcmd = "";
		if (!pathOfOutputFile.equals("")) {
			outputcmd = "\necho " + batOutputFileContentTermination + ">>\"" + pathOfOutputFile + "\"" + "\necho "
					+ tellClipOutputFilePath + pathOfOutputFile + tellClipNoError + "|clip\ndel %0";
		}

		String res = "";
		try {
			File f = new File(Path);
			if (!f.exists()) {
				Utility.createFile(f);
			} else {
				f.delete();
				Thread.sleep(2000);
				Utility.createFile(f);
			}
			f.setReadable(true);
			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);
			pw.write("");

			pw.append(runselfasadmin + content + outputcmd);

			pw.flush();
			pw.close();
			fw.close();
			f = null;
			String batfullpath = (new File(Path)).getAbsolutePath();
			res = batfullpath;

			Calendar calendar = Calendar.getInstance();
			Date current = new Date();
			calendar.setTime(current);
			calendar.add(Calendar.SECOND, 10);
			Date breakTime = calendar.getTime();
			File fnew = new File(batfullpath);

			while (!fnew.exists()) {

				Thread.sleep(1000);
				fnew = new File(batfullpath);
				current = new Date();
				if (current.after(breakTime)) {
					res = "";
					throw new IllegalArgumentException("failed to generate bat file: " + batfullpath);

				}
			}

			System.out.println("successful to generate bat file: " + batfullpath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	private boolean localToRemoteServer(WinType currentWT, String serverip, String username, String password,
			WinType nextWT) throws InterruptedException {
		chooseWindAutomationSolution(currentWT);
		String currentosanme = currentWT.getName();
		String nextosname = nextWT.getName();
		System.out.println("current os is " + currentosanme + ", target os is " + nextosname + ".");
		boolean remoteScr = false;
		String time = Utility.getCurrentTime().replaceAll(":", "-");
		rtsbatname = serverip + time + ".bat";
		rtsbatLocalpath = tempfilesPath + rtsbatname;

		String localbatPath = ltsbat(serverip, username, rtsbatLocalpath);

		if (!localbatPath.equals("")) {
			System.out.println("running localbat.bat on Local");
			String cmd = "cmd /c \"" + localbatPath + "\"";
			Utility.runProcess(cmd, 20);
			Calendar calendar = Calendar.getInstance();
			Date current = new Date();
			calendar.setTime(current);
			calendar.add(Calendar.SECOND, 300);
			Date breakTime = calendar.getTime();
			boolean isTS = false;
			String clipC = Utility.getSysClipboardText();
			while (!clipC.endsWith(tellClipNoError)) {

				if (clipC.endsWith(tellClipError2)) {
					isTS = true;
					throw new IllegalArgumentException("failed to run local.bat on Local:" + clipC);

				}

				Thread.sleep(1000);

				clipC = Utility.getSysClipboardText();
				current = new Date();
				if (current.after(breakTime)) {
					isTS = true;
					throw new IllegalArgumentException("failed to run local.bat on Local");

				}
			}

			Thread.sleep(100);
			if (isTS == false) {
				Screen currentscr = new Screen();

				Object[] credentialsInputWindow = isCredentialsWinPopUp(currentscr, serverip);
				File localbatFile = new File(rtsbatLocalpath);
				try {
					localbatFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!(credentialsInputWindow == null)) {

					Thread.sleep(100);
					if (inputPasswordAndConfirm((RegionAdv) credentialsInputWindow[0], password)) {
						chooseWindAutomationSolution(nextWT);
						remoteScr = waitRemoteServerShowed(currentscr, serverip);
						if (!remoteScr) {
							throw new IllegalArgumentException("remote desktop is not showed correcly.");
						} else {
							// open the rdpclip
							// winRunD("TASKLIST /FI \"IMAGENAME eq rdpclip*\" |
							// findstr \"rdpclip\" > nul && ( TASKKILL /FI
							// \"IMAGENAME eq rdpclip*\" /F
							// )&rdpclip.exe||rdpclip.exe");
						}

					} else {
						throw new IllegalArgumentException("failed to fill password.");
					}

				}

			}
		}

		return remoteScr;
	}

	/**
	 * when you are in a remote windows server through that you want to connect
	 * another win server.
	 * 
	 * @param currentosanme
	 * @param serverip
	 * @param username
	 * @param password
	 * @param nextosname
	 * @return
	 * @throws InterruptedException
	 */
	private boolean remoteToServer(WinType currentWT, String serverip, String username, String password, WinType nextWT)
			throws InterruptedException {
		chooseWindAutomationSolution(currentWT);
		String currentosanme = currentWT.getName();
		String nextosname = nextWT.getName();
		System.out.println("current os is " + currentosanme + ", target os is " + nextosname + ".");
		boolean res = false;
		String time = Utility.getCurrentTime().replaceAll(":", "-");
		rtsbatname = serverip + time + ".bat";
		rtsbatLocalpath = tempfilesPath + rtsbatname;
		// rtsbatPathOnServer = desktoppath + rtsbatname;

		String rstbatPath = rtsbat(serverip, username, rtsbatLocalpath);
		if (!rstbatPath.equals("")) {
			System.out.println("preparing rtsbat.bat on server");
			String openFolder = (new File(tempfilesPath)).getAbsolutePath();
			if (copyBATToCurrentScreenDesktopAndRun(openFolder, rtsbatname, 180)) {
				/*
				 * String deletertsbatonserver = "cmd /c if exist \"" +
				 * rtsbatPathOnServer + "\" (del \"" + rtsbatPathOnServer +
				 * "\")"; winRunByUsingClip(deletertsbatonserver);
				 */
				Screen currentscr = new Screen();
				Object[] credentialsInputWindow = isCredentialsWinPopUp(currentscr, serverip);
				File localbatFile = new File(rstbatPath);
				try {
					localbatFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (!(credentialsInputWindow == null)) {
					if (inputPasswordAndConfirm((RegionAdv) credentialsInputWindow[0], password)) {
						chooseWindAutomationSolution(nextWT);
						res = waitRemoteServerShowed(currentscr, serverip);
						if (!res) {
							throw new IllegalArgumentException("remote desktop is not showed correcly.");
						} else {
							// open the rdpclip
							// winRunD("TASKLIST /FI \"IMAGENAME eq rdpclip*\" |
							// findstr \"rdpclip\" > nul && ( TASKKILL /FI
							// \"IMAGENAME eq rdpclip*\" /F
							// )&rdpclip.exe||rdpclip.exe");
						}

					} else {
						throw new IllegalArgumentException("failed to fill password.");
					}

				}
			}
		}

		return res;
	}

	// must be used after login Jumpstation in full screen model.
	private boolean copyBATToCurrentScreenDesktopAndRun(String path, String fullname, int secondtime)
			throws InterruptedException {
		boolean res = false;
		Screen scr = new Screen();
		// delete existing rtsbat.bat file on JS
		String filePath = desktoppath + fullname;
		// check rdpclip.exe is launched.
		/*
		 * String cmd = "cmd /c if exist \"" + filePath + "\" (del \"" +
		 * filePath + "\")"; if (!winRun(cmd)) { throw new
		 * IllegalArgumentException( "failed to del existing " + fullname +
		 * " file on desktop of current server screen."); }
		 */
		if (copyFileOnLocal(path, fullname)) {

		} else {
			throw new IllegalArgumentException("failed to copy " + fullname + " of local:" + path);
		}

		Location loc = new Location(scr.w - 1, 50);
		Calendar calendarw = Calendar.getInstance();
		Date currentw = new Date();
		calendarw.setTime(currentw);
		calendarw.add(Calendar.SECOND, 180);
		Date breakTimew = calendarw.getTime();
		boolean copyDone = false;
		int recopyNumber = 0;
		int recopyNumberMax = 2;
		String cmd2 = "cmd /c if exist \"" + filePath + "\" (echo " + tellClipFileExist + "|clip)";
		Date lastCopytime = Utility.convertTimeFromStringToDate("2015-00-00 00:00:01", "yyyy-MM-dd HH:mm:ss");
		while (currentw.before(breakTimew)) {
			// paste on current screen
			if ((recopyNumber < recopyNumberMax)
					& (Utility.dateDiff(lastCopytime, currentw, "yyyy-MM-dd HH:mm:ss", "s") >= 30)) {
				System.out.println("paste " + fullname + " of local:" + path);
				minAllWindows();
				loc = new Location(scr.w - 1, 50);
				MouseAdv.click(loc, "L", 1);
				scr.keyDown(Key.CTRL);
				scr.keyDown("v");
				scr.keyUp("v");
				scr.keyUp(Key.CTRL);
				recopyNumber = recopyNumber + 1;
				lastCopytime = new Date();
				MatchAdv copyandreplace = advExists(scr, remotewintowinfulpath + "/copyandreplace.png", 10);
				if (copyandreplace != null) {
					try {
						scr.type(null, Key.TAB, 0);
						Location newloc = new Location(copyandreplace.x, copyandreplace.y);
						RegionAdv region = new RegionAdv(
								scr.newRegion(newloc, scr.w - copyandreplace.x, scr.h - copyandreplace.y));
						MatchAdv copyandreplaceCancel = advExists(region,
								remotewintowinfulpath + "/copyandreplaceCancel.png", 2);
						if (copyandreplaceCancel != null) {
							copyandreplaceCancel.click();
							loc = new Location(scr.w - 1, 50);
							MouseAdv.move(loc);
						}
					} catch (FindFailed e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				/*
				 * try { Thread.sleep(10000); } catch (InterruptedException e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); }
				 */
			} else {

				Thread.sleep(2000);

			}

			if (!Utility.getSysClipboardText().trim().equals(tellClipFileExist)) {
				if (!winRun(cmd2, 200)) {
					throw new IllegalArgumentException(
							"failed to run cmd on desktop of current server screen: " + cmd2);
				}
			} else {
				copyDone = true;
				break;
			}
			currentw = new Date();

		}

		if (copyDone) {
			System.out.println("successful to paste " + fullname + " on desktop of current server screen");
			File f = new File(path + "\\" + fullname);
			if (f.exists()) {
				f.delete();
			}
			String cmd3 = filePath;
			if (winRunByUsingClip(cmd3)) {
				Calendar calendar = Calendar.getInstance();
				Date current = new Date();
				calendar.setTime(current);
				calendar.add(Calendar.SECOND, secondtime);
				Date breakTime = calendar.getTime();
				boolean isTS = false;
				String clipC = Utility.getSysClipboardText();
				MatchAdv uacpopup = null;
				MatchAdv permissionactionmsg = null;
				boolean uacpopuphandled = false;
				boolean permissionpopuphandled = false;
				while (!clipC.endsWith(tellClipNoError)) {
					if (uacpopuphandled == false) {
						uacpopup = advExists(scr, remotewintowinfulpath + "/UACpopup.png", 2);
						if (!(uacpopup == null)) {
							MatchAdv uacyes = advExists(scr, remotewintowinfulpath + "/UACYes.png", 10);
							MatchAdv uacyes2 = advExists(scr, remotewintowinfulpath + "/UACYes2.png", 1);
							if ((uacyes == null) & (uacyes2 == null)) {
								isTS = true;
								throw new IllegalArgumentException(
										"failed to execution " + fullname + " with UAC allowed");

							} else {
								if (uacyes != null) {
									uacyes.click();
								} else {
									uacyes2.click();
								}
								loc = new Location(scr.w - 1, 50);
								MouseAdv.move(loc);
								System.out.println("successed to execution " + fullname + " with UAC allowed");
							}
							uacpopuphandled = true;
						}
					}

					if (permissionpopuphandled == false) {
						permissionactionmsg = advExists(scr, remotewintowinfulpath + "/permissionactionmsg.png", 2);
						if (!(permissionactionmsg == null)) {
							MatchAdv permissionactioncontinue1 = advExists(scr,
									remotewintowinfulpath + "/permissionactioncontinue1.png", 10);
							if (permissionactioncontinue1 == null) {
								MatchAdv permissionactioncontinue2 = advExists(scr,
										remotewintowinfulpath + "/permissionactioncontinue2.png", 2);
								if (permissionactioncontinue2 == null) {
									isTS = true;
									throw new IllegalArgumentException(
											"failed to execution " + fullname + " with permission allowed");

								} else {
									permissionactioncontinue2.click();
									System.out
											.println("successed to execution " + fullname + " with permission allowed");
								}
								loc = new Location(scr.w - 1, 50);
								MouseAdv.move(loc);
							} else {
								permissionactioncontinue1.click();
								System.out.println("successed to execution " + fullname + " with permission allowed");
							}
							permissionpopuphandled = true;
							loc = new Location(scr.w - 1, 50);
							MouseAdv.move(loc);
						}
					}

					if (clipC.endsWith(tellClipError2)) {
						isTS = true;
						throw new IllegalArgumentException(
								"failed to run " + fullname + " on  on desktop of current server screen:" + clipC);

					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					clipC = Utility.getSysClipboardText();
					current = new Date();
					if (current.after(breakTime)) {
						isTS = true;
						throw new IllegalArgumentException(
								"failed to run " + fullname + " on desktop of current server screen");

					}
				}
				if (isTS == false) {
					res = true;

					clipC = Utility.getSysClipboardText();

					if (clipC.startsWith("cpc@")) {
						String dir = clipC.substring(4, clipC.length() - 3);
						// copy file from remote to local and post to clip

						System.out.println("dir: " + dir);
						String[] nameArry = dir.split("\\\\");
						String fullnameRes = nameArry[nameArry.length - 1];
						String pathRes = dir.replaceFirst(fullnameRes, "");
						pathRes = pathRes.substring(0, pathRes.length() - 1);
						if (copyFileOnRemoteToLocalClipByBAT(pathRes, fullnameRes)) {
							res = true;
						} else {
							res = false;
							throw new IllegalArgumentException(
									"failed to copy file of current server screen to local clip: " + dir);
						}
						/*
						 * String cmdRR1 = "cmd /c if exist \"" + dir +
						 * "\" (del \"" + dir + "\")"; winRun(cmdRR1, 200);
						 */
					} else {
						res = true;
					}

				} else {
					throw new IllegalArgumentException("cannot get result of cmd: " + cmd3);
				}
			} else {
				throw new IllegalArgumentException(
						"failed to execute cmd on desktop of current server screen: " + cmd3);
			}

		} else {
			throw new IllegalArgumentException(
					"failed to paste " + fullname + " on desktop of current server screen:" + path);
		}

		return res;
	}

	private boolean waitRemoteServerShowed(Screen remoteScr, String serverip) throws InterruptedException {

		Thread.sleep(5000);

		System.out.println("Waiting screen for Remote Server: " + serverip);
		Location loc = new Location(0, 0);
		MouseAdv.move(loc);
		RegionAdv region = new RegionAdv(remoteScr.newRegion(loc, remoteScr.w, remoteScr.h / 8));
		boolean findRS = false;

		Calendar calendarw = Calendar.getInstance();
		Date currentw = new Date();
		calendarw.setTime(currentw);
		calendarw.add(Calendar.SECOND, 60);
		Date breakTimew = calendarw.getTime();
		MatchAdv creddidnotwork = null;
		MatchAdv remoteScrBar = null;
		MatchAdv connecting = null;
		Date curdate = new Date();
		while (curdate.before(breakTimew)) {

			Thread.sleep(100);
			connecting = advExists(remoteScr, remotewintowinfulpath + "/connecting1.png", 1);
			remoteScrBar = advExists(region, remotewintowinfulpath + "/remotescrbar.png", 1);
			if (!(remoteScrBar == null) && (connecting == null)) {
				// remoteScrBar.mouseMove();
				System.out.println("waiting remote screen - screen is showed");
				break;
			} else {
				System.out.println("waiting remote screen - screen is not showed");
			}
			creddidnotwork = advExists(remoteScr, remotewintowinfulpath + "/creddidnotwork.png", 1);
			if (creddidnotwork != null) {
				throw new IllegalArgumentException("Your crentials did not work");

			}

			Thread.sleep(900);

			curdate = new Date();
		}

		if (creddidnotwork == null) {

			if (!(remoteScrBar == null)) {
				Calendar calendaro = Calendar.getInstance();
				Date currento = new Date();
				calendaro.setTime(currento);
				calendaro.add(Calendar.SECOND, 60);
				Date breakTimeo = calendaro.getTime();
				MatchAdv okbutton = null;
				MatchAdv okbutton1 = null;
				MatchAdv okbutton2 = null;
				MatchAdv runwindow = null;
				boolean okbuttonHandled = false;
				while (currento.before(breakTimeo)) {

					Thread.sleep(100);
					if (okbuttonHandled == false) {
						okbutton1 = advExists(remoteScr, remotewintowinfulpath + "/oktologinremote1.png", 2);
						okbutton2 = advExists(remoteScr, remotewintowinfulpath + "/oktologinremote.png", 2);

						if (okbutton1 == null) {
							if (okbutton2 != null) {
								okbutton = okbutton2;
							}
						} else {
							okbutton = okbutton1;
						}
						if (!(okbutton == null)) {
							int clicked = okbutton.click();
							if (!(clicked == 1)) {
								throw new IllegalArgumentException(
										"failed to click OK button to enter Remote Server: " + serverip);
							} else {
								System.out.println("click OK button to enter Remote Server:" + serverip);

							}
							okbuttonHandled = true;

							Object[] result = restPasswordWhenFirstLogin(remoteScr);
							if ((boolean) result[0]) {
								if (!(boolean) result[1]) {
									throw new IllegalArgumentException(
											"failed to reset password when first logon Remote Server: " + serverip);

								} else {
									Calendar calendar = Calendar.getInstance();
									Date current = new Date();
									calendar.setTime(current);
									calendar.add(Calendar.SECOND, 60);
									breakTimeo = calendar.getTime();
								}
							}

						} else {
							System.out.println("no OK button to enter Remote Server:" + serverip);
						}
					}
					// showWind = advExists(remoteScr, remotewintowinfulpath +
					// "/showWind.png", 2);

					remoteScr.keyDown(Key.WIN);
					remoteScr.keyDown("r");
					remoteScr.keyUp("r");
					remoteScr.keyUp(Key.WIN);

					Thread.sleep(900);

					runwindow = advExists(remoteScr, remotewintowinfulpath + "/runwindow.png", 2);
					if (runwindow != null) {
						break;
					}

					currento = new Date();
				}

				if (!(runwindow == null)) {
					System.out.println("Remote Server screen is showed: " + serverip);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("double checking IP of Remote Server showed: " + serverip);
					minAllWindows();
					Calendar calendar = Calendar.getInstance();
					Date current = new Date();
					calendar.setTime(current);
					calendar.add(Calendar.SECOND, 60);
					Date breakTime = calendar.getTime();
					boolean isTS = false;
					String clipC = Utility.getSysClipboardText();
					while (!clipC.trim().contains(serverip)) {
						if (current.after(breakTime)) {
							isTS = true;
							throw new IllegalArgumentException(
									"Remote Server current showed is not IP wanted: " + serverip);

						}
						String cmd = "cmd /c ipconfig | clip";
						winRunByUsingClip(cmd);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						clipC = Utility.getSysClipboardText();
					}
					if (isTS == false) {
						findRS = true;
						System.out.println("Remote Server is ready for use: " + serverip);
					} else {
						if (!clipC.trim().contains(firstServerIP)) {
							String logoffcmd = "logoff";
							winRunByUsingClip(logoffcmd);
							throw new IllegalArgumentException("The server logged in is not server: " + serverip
									+ ". so logoff from the server: " + clipC.trim());
						}
					}

				} else {
					throw new IllegalArgumentException("failed to Remote Server screen: " + serverip + "("
							+ remotewintowinfulpath + "/runwindow.png)");
				}

			} else {
				throw new IllegalArgumentException(
						"Remote server screen is not showed after 60 seconds behind logon: " + serverip);
			}
		}
		loc = new Location(0, 0);
		MouseAdv.move(loc);
		return findRS;
	}

	private Object[] restPasswordWhenFirstLogin(Screen remoteScr) throws InterruptedException {
		Object res[] = { false, false, "" }; // {reset showed, reset
												// successful,password}
		Location loc = new Location(remoteScr.w - 1, 50);
		MouseAdv.click(loc, "L", 1);

		MatchAdv firstLoginWarn = advExists(remoteScr, remotewintowinfulpath + "/firstLoginWarn.png", 10);
		if (!(firstLoginWarn == null)) {
			System.out.println("reset password for current showed windows server");
			res[0] = true;
			MatchAdv firstLoginWarnOK = advExists(remoteScr, remotewintowinfulpath + "/firstLoginWarnOK.png", 2);
			firstLoginWarnOK.click();

			MouseAdv.click(loc, "L", 1);
			String newPWD = Config.NEWPASSWORD;

			MatchAdv firstLoginNewP = null;
			MatchAdv firstLoginConfirmP = null;
			// MatchAdv firstLoginResetPConfirm = null;
			Calendar calendar = Calendar.getInstance();
			Date current = new Date();
			calendar.setTime(current);
			calendar.add(Calendar.SECOND, 60);
			Date breakTime = calendar.getTime();

			boolean allshowed = false;
			while (current.before(breakTime)) {

				Thread.sleep(100);
				firstLoginNewP = advExists(remoteScr, remotewintowinfulpath + "/firstLoginNewP.png", 2);
				firstLoginConfirmP = advExists(remoteScr, remotewintowinfulpath + "/firstLoginConfirmP.png", 2);
				// firstLoginResetPConfirm = advExists(remoteScr,
				// remotewintowinfulpath + "/firstLoginResetPConfirm.png", 2);
				if (!allshowed & firstLoginNewP != null & firstLoginConfirmP != null) {
					allshowed = true;
					Calendar calendarnew = Calendar.getInstance();
					Date currentnew = new Date();
					calendarnew.setTime(currentnew);
					calendarnew.add(Calendar.SECOND, 10);
					breakTime = calendarnew.getTime();
				}
				MouseAdv.click(loc, "L", 1);
				current = new Date();
			}
			if (allshowed) {

				/*
				 * firstLoginNewP = advExists(remoteScr, remotewintowinfulpath +
				 * "/firstLoginNewP.png", 2); firstLoginConfirmP =
				 * advExists(remoteScr, remotewintowinfulpath +
				 * "/firstLoginConfirmP.png", 2); firstLoginResetPConfirm =
				 * advExists(remoteScr, remotewintowinfulpath +
				 * "/firstLoginResetPConfirm.png", 2);
				 */
				firstLoginNewP.click();
				loc = new Location(new Screen().w - 1, 50);
				MouseAdv.move(loc);
				Thread.sleep(1000);

				typeOneByOne(firstLoginNewP, newPWD, 200);

				Thread.sleep(5000);

				firstLoginConfirmP.click();
				loc = new Location(new Screen().w - 1, 50);
				MouseAdv.move(loc);
				Thread.sleep(1000);

				typeOneByOne(firstLoginConfirmP, newPWD + Key.ENTER, 200);

				Thread.sleep(5000);

				// firstLoginResetPConfirm.click();
				MatchAdv resetPWDMsg = null;
				MatchAdv resetPWDWarn1 = null;
				MatchAdv resetPWDWarn2 = null;
				MatchAdv resetPWDWarn3 = null;
				Calendar calendarR = Calendar.getInstance();
				Date currentR = new Date();
				calendarR.setTime(currentR);
				calendarR.add(Calendar.SECOND, 40);
				Date breakTimeR = calendarR.getTime();
				boolean isTS = true;
				while (currentR.before(breakTimeR)) {
					resetPWDMsg = advExists(remoteScr, remotewintowinfulpath + "/resetPWDMsg.png", 2);
					if (!(resetPWDMsg == null)) {
						MatchAdv resetPWDOk = advExists(remoteScr, remotewintowinfulpath + "/resetPWDOk.png", 2);
						resetPWDOk.click();
						loc = new Location(new Screen().w - 1, 50);
						MouseAdv.move(loc);
						res[1] = true;
						res[2] = newPWD;
						System.out.println("new password for current showed windows server is:" + newPWD);
						isTS = false;
						break;
					}

					resetPWDWarn1 = advExists(remoteScr, remotewintowinfulpath + "/resetPWDWarn1.png", 2);
					resetPWDWarn2 = advExists(remoteScr, remotewintowinfulpath + "/resetPWDWarn2.png", 2);
					resetPWDWarn3 = advExists(remoteScr, remotewintowinfulpath + "/resetPWDWarn3.png", 2);
					if ((resetPWDWarn1 != null) || (resetPWDWarn2 != null) || (resetPWDWarn3 != null)) {
						res[1] = false;
						res[2] = newPWD;
						System.out.println("failed to reset password for current showed windows server is:" + newPWD);

						loc = new Location(remoteScr.w - 1, 50);
						MouseAdv.move(loc);
						MatchAdv resetPWDOk = advExists(remoteScr, remotewintowinfulpath + "/resetPWDOk.png", 2);
						if (resetPWDOk != null) {
							resetPWDOk.click();
							throw new IllegalArgumentException("confirm reset password failed message");
						}

						loc = new Location(remoteScr.w - 1, 50);
						MouseAdv.move(loc);
						MatchAdv resetPWDCancel = advExists(remoteScr, remotewintowinfulpath + "/resetPWDCancel.png",
								10);
						if (resetPWDCancel != null) {
							resetPWDCancel.click();
							throw new IllegalArgumentException("leave the server");
						}
						loc = new Location(new Screen().w - 1, 50);
						MouseAdv.move(loc);
						break;
					}

					currentR = new Date();

				}
				if (isTS) {
					res[1] = false;
					res[2] = "";
					throw new IllegalArgumentException(
							"met unknown error to reset password for current showed windows server is:" + newPWD);
				}

			} else {
				System.out.println(
						"met unknown error when resetting password for current showed windows server: cannot match "
								+ remotewintowinfulpath);
			}
		}
		loc = new Location(0, 0);
		MouseAdv.move(loc);
		return res;
	}

	/**
	 * minimize windows desktop
	 */
	private void minAllWindows() {
		Screen currentScreen = new Screen(0);

		try {
			Thread.sleep(1000);
			currentScreen.keyDown(Key.WIN);
			currentScreen.keyDown("m");
			currentScreen.keyUp("m");
			currentScreen.keyUp(Key.WIN);
			// currentScreen.type(null, "m", KeyModifier.WIN);
			Thread.sleep(1000);
		} catch (Exception e) {
			throw new IllegalArgumentException("cannot open window + m");

		}
		// blockFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private int winRunRetry = 0;
	private int winRunRetryMax = 3;

	private boolean winRunByUsingClip(String cmd) throws InterruptedException {
		boolean res = false;
		Screen currentScreen = new Screen(0);
		MatchAdv runwindow = null;
		try {
			Thread.sleep(500);
			currentScreen.keyDown(Key.WIN);
			currentScreen.keyDown("r");
			currentScreen.keyUp("r");
			currentScreen.keyUp(Key.WIN);
			// currentScreen.type(null, "r", KeyModifier.WIN);
			Thread.sleep(1000);
			runwindow = advExists(currentScreen, remotewintowinfulpath + "/runwindow.png", 10);
			if (runwindow == null) {
				throw new IllegalArgumentException("cannot open window + R");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("cannot open window + R");

		}
		Utility.setSysClipboardText(cmd);
		// char[] cmdarr = cmd.toCharArray();
		try {

			Thread.sleep(1000);
			runwindow.keyDown(Key.ALT);
			runwindow.keyDown("a");
			runwindow.keyUp("a");
			runwindow.keyUp(Key.ALT);
			// runwindow.type(null, "a", KeyModifier.ALT);
			Thread.sleep(200);
			runwindow.keyDown(Key.BACKSPACE);
			runwindow.keyUp(Key.BACKSPACE);
			// runwindow.type(null, Key.BACKSPACE, 0);
			Thread.sleep(500);
			runwindow.keyDown(Key.CTRL);
			runwindow.keyDown("v");
			runwindow.keyUp("v");
			runwindow.keyUp(Key.CTRL);
			// runwindow.type(null, "v", KeyModifier.CTRL);

			Thread.sleep(200);
			runwindow.keyDown(Key.ENTER);
			runwindow.keyUp(Key.ENTER);
			// runwindow.type(null, Key.ENTER, 0);

			System.out.println("run CMD: " + cmd);
		} catch (Exception e) {
			throw new IllegalArgumentException("cannot run CMD: " + cmd);

		}
		MatchAdv cannotfind1 = advExists(currentScreen, remotewintowinfulpath + "/cannotfind1.png", 5);
		if (!(cannotfind1 == null)) {
			RegionAdv region = new RegionAdv(cannotfind1.x, cannotfind1.y, currentScreen.w - cannotfind1.x,
					currentScreen.y - cannotfind1.y);
			MatchAdv cannotfindOK = advExists(region, remotewintowinfulpath + "/cannotfindOK.png", 3);
			if (!(cannotfindOK == null)) {
				cannotfindOK.click();
			}
			Location loc = new Location(new Screen().w - 1, 50);
			MouseAdv.move(loc);
			if (winRunRetry == winRunRetryMax) {
				winRunRetry = 0;
				throw new IllegalArgumentException("cannot find the file or CMD: " + cmd);

			} else {
				winRunByUsingClip(cmd);
				winRunRetry = winRunRetry + 1;
				throw new IllegalArgumentException("cannot find the file or CMD, retry again: " + cmd);

			}

		} else {
			res = true;
		}
		currentScreen = null;
		winRunRetry = 0;
		return res;
	}

	/**
	 * run a command through windows' win-RUN
	 * 
	 * @param cmd
	 * @param interval
	 * @return
	 * @throws InterruptedException
	 */
	private boolean winRun(String cmd, int interval) throws InterruptedException {
		boolean res = false;
		Screen currentScreen = new Screen(0);
		MatchAdv runwindow = null;
		try {
			Thread.sleep(500);
			currentScreen.keyDown(Key.WIN);
			currentScreen.keyDown("r");
			currentScreen.keyUp("r");
			currentScreen.keyUp(Key.WIN);
			// currentScreen.type(null, "r", KeyModifier.WIN);
			Thread.sleep(1000);
			runwindow = advExists(currentScreen, remotewintowinfulpath + "/runwindow.png", 10);
			if (runwindow == null) {
				throw new IllegalArgumentException("cannot open window + R");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("cannot open window + R");

		}
		// Utility.setSysClipboardText(cmd);
		// char[] cmdarr = cmd.toCharArray();
		try {

			Thread.sleep(1000);
			runwindow.keyDown(Key.ALT);
			runwindow.keyDown("a");
			runwindow.keyUp("a");
			runwindow.keyUp(Key.ALT);
			// runwindow.type(null, "a", KeyModifier.ALT);
			Thread.sleep(200);
			runwindow.keyDown(Key.BACKSPACE);
			runwindow.keyUp(Key.BACKSPACE);

			Thread.sleep(1000);
			typeOneByOne(runwindow, cmd, interval);
			/*
			 * for (int i = 0; i < cmdarr.length; i++) { //
			 * Settings.TypeDelay=0.03; String var = String.valueOf(cmdarr[i]);
			 * a: switch (var) { case "|": runwindow.type("|",
			 * KeyModifier.SHIFT); break a; case "\"": runwindow.type("'",
			 * KeyModifier.SHIFT); break a; default: runwindow.type(var); } }
			 */
			// Settings.TypeDelay=0;

			Thread.sleep(200);
			runwindow.keyDown(Key.ENTER);
			runwindow.keyUp(Key.ENTER);
			// runwindow.type(null, Key.ENTER, 0);

			System.out.println("run CMD: " + cmd);
		} catch (Exception e) {
			throw new IllegalArgumentException("cannot run CMD: " + cmd);

		}
		MatchAdv cannotfind1 = advExists(currentScreen, remotewintowinfulpath + "/cannotfind1.png", 5);
		if (!(cannotfind1 == null)) {
			RegionAdv region = new RegionAdv(cannotfind1.x, cannotfind1.y, currentScreen.w - cannotfind1.x,
					currentScreen.y - cannotfind1.y);
			MatchAdv cannotfindOK = advExists(region, remotewintowinfulpath + "/cannotfindOK.png", 3);
			if (!(cannotfindOK == null)) {
				cannotfindOK.click();
			}
			Location loc = new Location(new Screen().w - 1, 50);
			MouseAdv.move(loc);
			if (winRunRetry == winRunRetryMax) {
				winRunRetry = 0;
				throw new IllegalArgumentException("cannot find the file or CMD: " + cmd);

			} else {
				winRunByUsingClip(cmd);
				winRunRetry = winRunRetry + 1;
				throw new IllegalArgumentException("cannot find the file or CMD, retry again: " + cmd);

			}

		} else {
			res = true;
		}
		currentScreen = null;
		winRunRetry = 0;
		return res;
	}

	private String runBATThroughWinRun(String contentOfBAT, String pathOfOutputFile) throws InterruptedException {
		String res = "";
		Utility.setSysClipboardText("");

		Thread.sleep(1000);

		String time = Utility.getCurrentTime().replaceAll(":", "-");
		String newnormalbatname = "cmd" + time + ".bat";
		String newnormalbatbatLocalpath = tempfilesPath + newnormalbatname;
		String restmpfile = tempfilesPath + "res for cmd" + time + ".xml";

		String nbatPath = normalbat(contentOfBAT, newnormalbatbatLocalpath, pathOfOutputFile);
		if (!nbatPath.equals("")) {
			System.out.println("preparing cmd.bat on local server");
			String openFolder = (new File(tempfilesPath)).getAbsolutePath();
			if (copyBATToCurrentScreenDesktopAndRun(openFolder, newnormalbatname, 180)) {
				System.out.println("convert content of clip to local string");
				String result = Utility.getSysClipboardText().trim();
				if (result.length() < ("\n" + batOutputFileContentTermination).length()) {
					res = result.substring(0);
					throw new RuntimeException(new Exception(
							"the content is not end of  \"" + batOutputFileContentTermination + "\":\n" + res));
				} else {
					if (result.contains(batOutputFileContentTermination)) {
						res = result.substring(0, result.length() - ("\n" + batOutputFileContentTermination).length());
					} else {
						res = result.substring(0);
						throw new RuntimeException(new Exception(
								"the content is not end of  \"" + batOutputFileContentTermination + "\":\n" + res));
					}

				}
				/*
				 * //scan all file is slow String resAfterFormat = ""; String
				 * strCur = ""; if (!result.equals("")) { // remove mass code of
				 * the result!!! for (int z = 0; z < res.length(); z++) { strCur
				 * = res.substring(z, z + 1); if
				 * (!strCur.matches("[\u4e00-\u9fa5]+")) { // chinese interval
				 * if (strCur.matches("[\\x00-\\x7F]+")) { // ASCII interval
				 * resAfterFormat = resAfterFormat + strCur; } } } res =
				 * resAfterFormat; }
				 */
				// process mass word of "<servermanagerconfiguration>"
				if (!res.equals("")) {
					String spliter = "<servermanagerconfiguration>";
					int index = res.indexOf(spliter);
					if (index > -1) {
						int startIndex = spliter.length() + index;

						String subRes = res.substring(startIndex);
						int endIndex = subRes.indexOf("ServerManagerConfigurationQuery");
						if (endIndex > -1) {
							subRes = subRes.substring(0, endIndex);
							res = res.substring(0, startIndex) + "<" + res.substring(startIndex + subRes.length());
						}
					}
				}

				File f = new File(restmpfile);
				try {
					Utility.createFile(f);
					f.setReadable(true);
					FileWriter fw = new FileWriter(f);
					PrintWriter pw = new PrintWriter(fw);
					pw.write(res);
					pw.flush();
					pw.close();
					fw.close();
					System.out.println("get result of bat and store in the folder:" + restmpfile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				f = null;
			} else {
				throw new IllegalArgumentException("failed to run cmd.bat on desktop of current server screen");
			}
		} else {
			throw new IllegalArgumentException("failed to generate file cmd.bat");
		}

		/*
		 * String deletenormalbatonserver = "cmd /c if exist \"" +
		 * newnormalbatbatPathOnServer + "\" (del \"" +
		 * newnormalbatbatPathOnServer + "\")";
		 * winRunByUsingClip(deletenormalbatonserver);
		 */
		return res;
	}

	private Object[] isCredentialsWinPopUp(Screen scr, String serverip) throws InterruptedException {
		Object[] res = { null, null };
		String regx = "[0-9].*[0-9]";
		RegionAdv regionConnectAny = null;
		Calendar calendar = Calendar.getInstance();
		Date current = new Date();
		calendar.setTime(current);
		calendar.add(Calendar.SECOND, 60);
		Date breakTime = calendar.getTime();
		MatchAdv harmerror = null;
		boolean harmerrorHandled = false;
		MatchAdv certiferror = null;
		boolean certiferrorHandled = false;
		MatchAdv entercred1 = null;
		MatchAdv rdpError1 = null;
		MatchAdv rdpError2 = null;
		boolean isTS = true;
		while ((new Date()).before(breakTime)) {

			Thread.sleep(100);
			entercred1 = advExists(scr, remotewintowinfulpath + "/entercred1.png", 1);
			if (!(entercred1 == null)) {
				entercred1.click();
				Location loc = new Location(new Screen().w - 1, 50);
				MouseAdv.move(loc);
				scr.keyDown(Key.TAB);
				scr.keyUp(Key.TAB);
				res[0] = new RegionAdv(entercred1.x, entercred1.y, scr.w - 1, scr.h - entercred1.y);
				RegionAdv contentip = new RegionAdv(entercred1.x, entercred1.y, 2 * entercred1.w,
						entercred1.y + (entercred1.y / 2));
				res[1] = Utility.RegGetString(contentip.text(), regx);
				System.out.println("the IP which is showed on the credentials window is " + res[1]);
				isTS = false;
				break;
			}

			harmerror = advExists(scr, remotewintowinfulpath + "/harmerror.png", 1);
			if (!(harmerror == null) && (harmerrorHandled == false)) {
				harmerror.click();
				Location loc = new Location(new Screen().w - 1, 50);
				MouseAdv.move(loc);
				System.out.println("found connection harm error.");
				regionConnectAny = new RegionAdv(harmerror.x, harmerror.y, scr.w - 1 - harmerror.x,
						scr.h - harmerror.y - 1);

				MatchAdv connectanywayConnect1 = advExists(regionConnectAny,
						remotewintowinfulpath + "/connectanywayConnect1.png", 1);

				if (!(connectanywayConnect1 == null)) {
					connectanywayConnect1.click();
					loc = new Location(new Screen().w - 1, 50);
					MouseAdv.move(loc);
					System.out.println("click connect anyway.");
				} else {
					MatchAdv connectanywayConnect = advExists(regionConnectAny,
							remotewintowinfulpath + "/connectanywayConnect.png", 1);
					if (!(connectanywayConnect == null)) {
						connectanywayConnect.click();
						loc = new Location(new Screen().w - 1, 50);
						MouseAdv.move(loc);
						System.out.println("click connect anyway.");
					} else {
						throw new IllegalArgumentException("failed to find connect button on connect anyway window");
					}
				}
				harmerrorHandled = true;
			}

			certiferror = advExists(scr, remotewintowinfulpath + "/certiferror.png", 1);
			if (!(certiferror == null) && (certiferrorHandled == false)) {
				System.out.println("found certiferror.");
				RegionAdv certiferrorR = new RegionAdv(certiferror.x, certiferror.y, scr.w - 1, scr.h - certiferror.y);
				MatchAdv certiferrorYes = advExists(certiferrorR, remotewintowinfulpath + "/certifyes.png", 5);
				int clicked = certiferrorYes.click();
				Location loc = new Location(new Screen().w - 1, 50);
				MouseAdv.move(loc);
				if (clicked == 1) {
					System.out.println("confirm certiferror anyway.");
				} else {
					throw new IllegalArgumentException("cannot found yes button on certiferror.");
				}

				certiferrorHandled = true;
			}

			rdpError1 = advExists(scr, remotewintowinfulpath + "/RDPCannotBeConnected.png", 1);
			if (!(rdpError1 == null)) {
				isTS = false;
				throw new IllegalArgumentException("Server cannot be connected.");

			}

			rdpError2 = advExists(scr, remotewintowinfulpath + "/invalidcf.png", 1);
			if (!(rdpError2 == null)) {
				isTS = false;
				throw new IllegalArgumentException("Invalid connection file.");

			}

		}
		if (isTS) {
			throw new IllegalArgumentException("unknown remote connection issue:" + serverip);
		}

		return res;
	}

	private boolean inputPasswordAndConfirm(RegionAdv credentialsInputWindow, String password)
			throws InterruptedException {
		boolean res = false;
		MatchAdv credpwdinput = advExists(credentialsInputWindow, remotewintowinfulpath + "/credpwdinput.png", 20);

		if (credpwdinput == null) {
			throw new IllegalArgumentException("input for password is not showed");
		} else {
			credpwdinput.click();
			Location loc = new Location(new Screen().w - 1, 50);
			MouseAdv.move(loc);
			Utility.setSysClipboardText(password);

			try {
				Thread.sleep(1000);
				credentialsInputWindow.keyDown(Key.CTRL);
				credentialsInputWindow.keyDown("v");
				credentialsInputWindow.keyUp("v");
				credentialsInputWindow.keyUp(Key.CTRL);
				// credentialsInputWindow.type(null, "v", KeyModifier.CTRL);
				Thread.sleep(500);
				credentialsInputWindow.keyDown(Key.ENTER);
				credentialsInputWindow.keyUp(Key.ENTER);
				// credentialsInputWindow.type(null, Key.ENTER, 0);
				Thread.sleep(500);
				res = true;
				System.out.println("input password to log on Remote Server.");
			} catch (Exception e) {
				throw new IllegalArgumentException("failed to enter password.");

			}
		}

		return res;
	}

	private boolean copyFileOnLocal(String path, String fullname) {
		boolean res = false;

		int exitCode = -1;

		String filePath = (new File(copyfileplugpath)).getAbsolutePath();
		// exitCode = Integer.parseInt(Utility.runProcess("explorer \"" + path +
		// "\"", 30)[1]);
		Utility.setSysClipboardText(path + "\\" + fullname);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		exitCode = Integer.parseInt(Utility.runProcess(filePath, 30)[1]);
		// System.out.println(String.valueOf(exitCode));
		if (exitCode <= 1) {
			/*
			 * try { Thread.sleep(5000); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); } Screen scr
			 * = new Screen(); try { typeOneByOne(scr, fullname);
			 * Thread.sleep(1000); scr.keyDown(Key.CTRL); scr.keyDown("c");
			 * scr.keyUp("c"); scr.keyUp(Key.CTRL); // scr.type(null, "c",
			 * KeyModifier.CTRL); } catch (Exception e1) { throw new
			 * IllegalArgumentException("Failed to copy file:" + path +"\\"+
			 * fullname); // e1.printStackTrace(); }
			 */
			Calendar calendar = Calendar.getInstance();
			Date current = new Date();
			calendar.setTime(current);
			calendar.add(Calendar.SECOND, 30);
			Date breakTime = calendar.getTime();
			boolean isTS = false;

			List<File> arrlist = Utility.getFileListClipboard();
			boolean copystatus = false;
			try {
				copystatus = arrlist.get(0).getName().equals(fullname);
			} catch (Exception e) {
				copystatus = false;
			}

			while (!copystatus) {
				if (current.after(breakTime)) {
					isTS = true;
					throw new IllegalArgumentException("Failed to copy file:" + path + "\\" + fullname);

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				current = new Date();
				arrlist = Utility.getFileListClipboard();
				try {
					copystatus = arrlist.get(0).getName().equals(fullname);
				} catch (Exception e) {
					copystatus = false;
				}
			}
			if (isTS == false) {
				System.out.println("successful to copy " + fullname + " on local: " + path);
				/*
				 * try { Thread.sleep(1000); scr.keyDown(Key.ALT);
				 * scr.keyDown(Key.F4); scr.keyUp(Key.F4); scr.keyUp(Key.ALT);
				 * // scr.type(null, Key.F4, KeyModifier.ALT);
				 * Thread.sleep(1000); } catch (Exception e) { throw new
				 * IllegalArgumentException("Failed to close folder" + path
				 * +"\\"+ fullname); e.printStackTrace(); }
				 */
				res = true;
			}
		}

		return res;
	}

	private boolean copyFileOnRemoteToLocalClipByBAT(String path, String fullname) throws InterruptedException {
		boolean res = false;
		Screen scr = new Screen();
		String filePathTBC = path + "\\" + fullname;
		String strContent = "@echo off\ncall :c2cb " + filePathTBC
				+ "\ndel %0\n\n::Copy2ClipBoard\n:c2cb\nsetlocal\nset \"f=%~1\"\nset \"f=%f:\\\\=\\%\"\nset \"f=%f:\\=\\\\%\"\nmshta \"javascript:moveTo(screen.width,0);document.write('<img id=\\'x\\' src=\\'%f%\\'>');i=document.body.createControlRange();i.add(x);i.execCommand('copy');close()\"\ngoto :eof";
		// System.out.println(strContent);
		String time = Utility.getCurrentTime().replaceAll(":", "-");
		String newnormalbatname = "cmd" + time + ".bat";
		String newnormalbatbatLocalpath = tempfilesPath + newnormalbatname;

		String copy2ClipBATPath = normalbat(strContent, newnormalbatbatLocalpath, "");
		String copyfolderpath = copy2ClipBATPath.replaceFirst(newnormalbatname, "");
		copyfolderpath = copyfolderpath.substring(0, copyfolderpath.length() - 1);

		if (copyFileOnLocal(copyfolderpath, newnormalbatname)) {

		} else {
			throw new IllegalArgumentException("failed to copy " + newnormalbatname + " of local:" + copyfolderpath);
		}

		Location locR = new Location(scr.w - 1, 50);
		Calendar calendarwR = Calendar.getInstance();
		Date currentwR = new Date();
		calendarwR.setTime(currentwR);
		calendarwR.add(Calendar.SECOND, 180);
		Date breakTimewR = calendarwR.getTime();
		boolean copyDoneR = false;
		int recopyNumberR = 0;
		int recopyNumberMaxR = 2;
		String filePathR = desktoppath + newnormalbatname;
		String cmd2R = "cmd /c if exist \"" + filePathR + "\" (echo " + tellClipFileExist + "|clip)";
		Date lastCopytimeR = Utility.convertTimeFromStringToDate("2015-00-00 00:00:01", "yyyy-MM-dd HH:mm:ss");
		while (currentwR.before(breakTimewR)) {
			// paste on current screen
			if ((recopyNumberR < recopyNumberMaxR)
					& (Utility.dateDiff(lastCopytimeR, currentwR, "yyyy-MM-dd HH:mm:ss", "s") >= 30)) {
				System.out.println("paste " + newnormalbatname + " to current server from local:" + copy2ClipBATPath);
				scr.keyDown(Key.WIN);
				scr.keyDown("m");
				scr.keyUp("m");
				scr.keyUp(Key.WIN);
				locR = new Location(scr.w - 1, 50);
				MouseAdv.click(locR, "L", 1);
				scr.keyDown(Key.CTRL);
				scr.keyDown("v");
				scr.keyUp("v");
				scr.keyUp(Key.CTRL);
				recopyNumberR = recopyNumberR + 1;
				lastCopytimeR = new Date();
				MatchAdv copyandreplace = advExists(scr, remotewintowinfulpath + "/copyandreplace.png", 10);
				if (copyandreplace != null) {
					try {
						scr.type(null, Key.TAB, 0);
						Location newloc = new Location(copyandreplace.x, copyandreplace.y);
						RegionAdv region = new RegionAdv(
								scr.newRegion(newloc, scr.w - copyandreplace.x, scr.h - copyandreplace.y));
						MatchAdv copyandreplaceCancel = advExists(region,
								remotewintowinfulpath + "/copyandreplaceCancel.png", 2);
						if (copyandreplaceCancel != null) {
							copyandreplaceCancel.click();
							Location loc = new Location(new Screen().w - 1, 50);
							MouseAdv.move(loc);
						}
					} catch (FindFailed e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				/*
				 * try { Thread.sleep(10000); } catch (InterruptedException e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); }
				 */
			} else {

				Thread.sleep(2000);

			}

			if (!Utility.getSysClipboardText().trim().equals(tellClipFileExist)) {
				if (!winRun(cmd2R, 200)) {
					throw new IllegalArgumentException(
							"failed to run cmd on desktop of current server screen: " + cmd2R);
				}
			} else {
				copyDoneR = true;
				break;
			}
			currentwR = new Date();

		}

		if (copyDoneR) {
			System.out.println("try to copy " + path + "\\" + fullname);

			String filePath = desktoppath + newnormalbatname;

			Calendar calendar = Calendar.getInstance();
			Date current = new Date();
			calendar.setTime(current);
			calendar.add(Calendar.SECOND, 180);
			Date breakTime = calendar.getTime();
			boolean isTS = false;

			List<File> arrlist = null;
			boolean copystatus = false;

			while (!copystatus) {

				if (current.after(breakTime)) {
					isTS = true;
					break;
				}

				winRun(filePath, 200);

				Thread.sleep(20000);

				current = new Date();
				arrlist = Utility.getFileListClipboard();
				try {
					copystatus = !arrlist.get(0).getName().equals("");
				} catch (Exception e) {
					copystatus = false;
				}
			}
			if (isTS == false) {

				int exitCode = -1;
				String copyfileplugabopath = (new File(copyfileplugpath)).getAbsolutePath();
				exitCode = Integer.parseInt(Utility.runProcess(copyfileplugabopath + " -cfcfrtlc", 180)[1]);
				if (exitCode <= 1) {
					Calendar calendarN = Calendar.getInstance();
					Date currentN = new Date();
					calendarN.setTime(currentN);
					calendarN.add(Calendar.SECOND, 180);
					Date breakTimeN = calendarN.getTime();
					boolean isTSN = false;
					String clipC = Utility.getSysClipboardText();
					MatchAdv uacpopup = null;
					MatchAdv permissionactionmsg = null;
					boolean uacpopuphandled = false;
					boolean permissionpopuphandled = false;
					while (!clipC.endsWith(tellClipNoError)) {
						if (uacpopuphandled == false) {
							uacpopup = advExists(scr, remotewintowinfulpath + "/UACpopup.png", 2);
							if (!(uacpopup == null)) {
								MatchAdv uacyes = advExists(scr, remotewintowinfulpath + "/UACYes.png", 10);
								MatchAdv uacyes2 = advExists(scr, remotewintowinfulpath + "/UACYes2.png", 1);
								if ((uacyes == null) & (uacyes2 == null)) {
									isTSN = true;
									throw new IllegalArgumentException(
											"failed to execution " + fullname + " with UAC allowed");

								} else {
									if (uacyes != null) {
										uacyes.click();
									} else {
										uacyes2.click();
									}
									Location loc = new Location(new Screen().w - 1, 50);
									MouseAdv.move(loc);
									System.out.println("successed to execution " + fullname + " with UAC allowed");
								}
								uacpopuphandled = true;
							}
						}

						if (permissionpopuphandled == false) {
							permissionactionmsg = advExists(scr, remotewintowinfulpath + "/permissionactionmsg.png", 2);
							if (!(permissionactionmsg == null)) {
								MatchAdv permissionactioncontinue1 = advExists(scr,
										remotewintowinfulpath + "/permissionactioncontinue1.png", 10);
								if (permissionactioncontinue1 == null) {
									MatchAdv permissionactioncontinue2 = advExists(scr,
											remotewintowinfulpath + "/permissionactioncontinue2.png", 2);
									if (permissionactioncontinue2 == null) {
										isTSN = true;
										throw new IllegalArgumentException(
												"failed to execution " + fullname + " with permission allowed");

									} else {
										permissionactioncontinue2.click();
										Location loc = new Location(new Screen().w - 1, 50);
										MouseAdv.move(loc);
										System.out.println(
												"successed to execution " + fullname + " with permission allowed");
									}

								} else {
									permissionactioncontinue1.click();
									Location loc = new Location(new Screen().w - 1, 50);
									MouseAdv.move(loc);
									System.out
											.println("successed to execution " + fullname + " with permission allowed");
								}
								permissionpopuphandled = true;
							}
						}

						Thread.sleep(20000);

						clipC = Utility.getSysClipboardText();
						currentN = new Date();
						if (currentN.after(breakTimeN)) {
							isTSN = true;
							throw new IllegalArgumentException(
									"failed to run " + fullname + " on desktop of current server screen");

						}
					}
					if (isTSN == false) {
						System.out.println(
								"successful to copy " + fullname + " on current server: " + path + " to local clip");
						res = true;
					} else {
						System.out.println(
								"Failed to copy " + fullname + " on current server: " + path + " to local clip");
					}

				} else {
					throw new IllegalArgumentException(
							"Failed to run " + copyfileplugabopath + " -cfcfrtlc" + " on current server");
				}

			} else {
				throw new IllegalArgumentException("Failed to copy file: " + path + "\\" + fullname + " by file "
						+ filePath + " on current screen");
			}
			String cmdRR1 = "cmd /c if exist \"" + filePath + "\" (del \"" + filePath + "\")";
			winRun(cmdRR1, 200);
		} else {
			throw new IllegalArgumentException(
					"failed to paste " + newnormalbatname + " on desktop of current server screen:" + copyfolderpath);
		}

		return res;
	}

	private MatchAdv advExists(Region yourScr, String imagePath, double timeout) throws InterruptedException {
		// Region scr = yourScr;

		Calendar calendar = Calendar.getInstance();
		Date current = new Date();
		calendar.setTime(current);
		calendar.add(Calendar.SECOND, (int) timeout + 1);
		Date breakTime = calendar.getTime();
		MatchAdv m = null;
		Screen scr = new Screen(0);
		a: while (current.before(breakTime)) {

			Thread.sleep(100);
			// System.out.println("find match !!!");
			Pattern p = new Pattern(imagePath);
			p = p.exact();

			try {
				m = new MatchAdv(scr.exists(p, 3));
			} catch (Throwable th) {
				// System.out.println("null");
				// th.printStackTrace();
				// System.out.println("msg:" + th.getMessage());
				// System.out.println("no exists" + imagePath);
			}

			if (m == null) {
				// System.out.println("try adv find");
				Pattern pi = new Pattern(imagePath);
				p = p.similar((float) 0.3);
				try {

					Iterator<Match> iterator = scr.findAll(p);

					while (iterator.hasNext()) {
						// System.out.println("finding");
						MatchAdv find = compareText(iterator.next(), pi.getImage());
						// System.out.println("finding end");

						if (!(find == null)) {
							m = find;
							break a;
						}
					}

					iterator = null;
				} catch (Throwable th) {
					// TODO Auto-generated catch block
					// th.printStackTrace();
					// System.out.println("msg:" + th.getMessage());

					// System.out.println("no findAll" + imagePath);
				}

			} else {
				m.highlight(1, "#000255000");
				break a;
			}
			// System.out.println("try adv find end");
			current = new Date();
			p = null;
			// System.out.println("find match end!!!");
			scr = null;
		}

		return m;
	}

	private MatchAdv compareText(Match m, Image n) {

		MatchAdv w = null;
		// m.highlight(1, "#255255000");
		String matchT = m.text().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
		String imageT = n.text().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
		// System.out.println("???[MatchAdvance]:" + matchT + " [Image]:" +
		// imageT);

		if ((!matchT.trim().equals("")) && (!imageT.trim().equals(""))) {
			if (matchT.contains(imageT)) {
				w = new MatchAdv(m);
				w.highlight(1, "#000255000");
				// System.out.println("!!![MatchStr]:" + matchT + " [ImageStr]:"
				// + imageT);
			}
		}

		return w;
	}

	/*
	 * private void typeOneByOne(RegionAdv region, String word) {
	 * typeOneByOne(region, word, 50);
	 * 
	 * }
	 */
	private void typeOneByOne(Region region, String word, int interval) {
		for (int i = 0; i < word.length(); i++) {
			String single = word.substring(i, i + 1);

			region.keyDown(single);
			region.keyUp(single);

			if (interval > 0) {
				try {
					Thread.sleep(interval);// interval is in minseconds
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private static class MouseAdv {
		public static void click(Location loc, String action, Integer args) {

			Mouse.click(loc, action, args);

		}

		public static void move(Location loc) {
			Mouse.move(loc);
		}

	}

	private class MatchAdv extends Match {

		public MatchAdv(Match m) {
			super(m);
			// TODO Auto-generated constructor stub
		}

		protected MatchAdv(int x, int y, int w, int h, double Score, IScreen parent, String text) {
			super(x, y, w, h, Score, parent, text);
			// TODO Auto-generated constructor stub
		}

		protected MatchAdv(FindResult f, IScreen _parent) {
			super(f, _parent);
			// TODO Auto-generated constructor stub
		}

		public MatchAdv(RegionAdv reg, double sc) {
			super(reg, sc);
			// TODO Auto-generated constructor stub
		}

		public int click() {

			int res = super.click();

			return res;
		}

		public void keyDown(String keys) {

			super.keyDown(keys);

		}

		public void keyUp(String keys) {

			super.keyUp(keys);

		}

	}

	private class RegionAdv extends Region {

		public RegionAdv(int x, int y, int w, int h) {
			super(x, y, w, h);

		}

		public RegionAdv(Region r) {
			super(r.x, r.y, r.w, r.h);
		}

		public int click() {

			int res = super.click();

			return res;
		}

		public void keyDown(String keys) {

			super.keyDown(keys);

		}

		public void keyUp(String keys) {

			super.keyUp(keys);

		}

	}

}
