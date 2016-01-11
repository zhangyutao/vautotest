package basic.windows;

import basic.Config;

public enum WinBatTemplate {
	win2008(Config.RESOURCEPATH + "/win/allinfo2008.bat"), win2012(Config.RESOURCEPATH + "/win/allinfo2012.bat");
	
	private String path;

	private WinBatTemplate(String path) {
		this.path = path;

	}

	public String getPath() {
		return path;
	}
}
