package basic.windows;

import basic.Config;

public enum WinResolution {
	win2008of1920x1080(Config.SIKULIX_PATH + "\\remotewin2008\\1920x1080"), win2012of1920x1080(
			Config.SIKULIX_PATH + "\\remotewin2012\\1920x1080");

	private String path;

	private WinResolution(String path) {
		this.path = path;

	}

	public String getPath() {
		return path;
	}
}
