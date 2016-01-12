package basic.winrd;

import java.io.File;
import basic.Config;

/**
 * Use for instancing your self bat file for {@link #WinRDAuto} to use.
 * 
 * @author zhangyutao
 *
 */
public class WinBat {
	private static String templatefolder = Config.RESOURCEPATH + "/win/";

	File batFile = null;
	String pathOfOutputFile = "";

	/**
	 * Set your own bat file
	 * 
	 * @param batFile
	 * @param pathOfOutputFile
	 *            - It must be absolute path. The path of file which is written
	 *            in first parameter "batFile" @see #batFile to get all output.
	 */
	public WinBat(File batFile, String pathOfOutputFile) {
		this.batFile = batFile;
		this.pathOfOutputFile = pathOfOutputFile;

	}

	public String getPathOfOutputFile() {
		return this.pathOfOutputFile;
	}

	public File getBatFile() {
		return this.batFile;
	}

	/**
	 * return current Template used by the WinAuto {@link #WinAuto} .
	 * 
	 * @param wbt
	 * @return
	 */
	public static File getTemplate(WinBatTemplate wbt) {
		String filePath = "";
		switch (wbt) {
		case W2008ALLINFO:
			filePath = templatefolder + "allinfo2008.bat";
			break;
		case W2012ALLINFO:
			filePath = templatefolder + "allinfo2012.bat";
			break;
		default:
			throw new IllegalArgumentException("Error parameter");
		}
		return new File(filePath);
	}
}
