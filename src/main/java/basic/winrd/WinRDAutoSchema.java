package basic.winrd;

import basic.Config;

/**
 * provide the information which {@link #WinRDAuto} support.
 * 
 * @author zhangyutao
 *
 */
public enum WinRDAutoSchema {
	W2008R1920X1080(WinType.win2008, WinResolution.R1920X1080, WinBatTemplate.W2008ALLINFO), W2012R1920X1080(
			WinType.win2012, WinResolution.R1920X1080, WinBatTemplate.W2012ALLINFO);

	private String imagePath = "";
	private WinType winType;
	private WinBatTemplate defaultWinBat;

	public WinBatTemplate getDefaultWinBat() {
		return defaultWinBat;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public WinType getWinType() {
		return winType;
	}

	private WinRDAutoSchema(WinType wt, WinResolution resolution, WinBatTemplate wbt) {
		this.winType = wt;
		this.defaultWinBat = wbt;
		switch (wt) {
		case win2008:
			switch (resolution) {
			case R1920X1080:
				this.imagePath = Config.SIKULIX_PATH + "\\remotewin2008\\1920x1080";
				break;
			default:
				break;
			}
			break;
		case win2012:
			switch (resolution) {
			case R1920X1080:
				this.imagePath = Config.SIKULIX_PATH + "\\remotewin2012\\1920x1080";
				break;
			default:
				break;
			}

			break;
		default:
			break;
		}

	}

}
