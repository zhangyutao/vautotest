package basic.windows;

import java.io.File;

import basic.Config;

public class WindowsBAT {
	private static String templatefolder = Config.RESOURCEPATH + "/win/";
	File batFile = null;

	public static File getTemplate(WinBatTemplate wbt) {
		String filePath = "";
		switch (wbt) {
		case win2008:
			filePath = templatefolder + "allinfo2008.bat";
			break;
		case win2012:
			filePath = templatefolder + "allinfo2012.bat";
			break;
		default:
			throw new IllegalArgumentException("Error parameter");
		}
		return new File(filePath);
	}

	public void setBatFile(File filepath) {
		this.batFile = filepath;
	}

	public File getBatFile() {
		return this.batFile;
	}
}
