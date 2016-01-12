package basic.winrd;

import basic.Config;

/**
 * provide the information of template which {@link #WinRDAuto} supports.
 * 
 * @author zhangyutao
 *
 */
public enum WinBatTemplate {
	W2008ALLINFO(Config.RESOURCEPATH + "/win/allinfo2008.bat"), W2012ALLINFO(
			Config.RESOURCEPATH + "/win/allinfo2012.bat");

	private String path;

	private WinBatTemplate(String path) {
		this.path = path;

	}

	public String getPath() {
		return path;
	}
}
