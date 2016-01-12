package basic.windows;

import basic.Config;

public enum WinResolution {
	W2008R1920X1080(Config.SIKULIX_PATH + "\\remotewin2008\\1920x1080"), W2012R1920X1080(
			Config.SIKULIX_PATH + "\\remotewin2012\\1920x1080");

	private String path;

	private WinResolution(String path) {
		this.path = path;

	}

	public String getPath() {
		return path;
	}
}
