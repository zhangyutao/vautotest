package basic.windows;

public enum WinType {
	win2008("2008"), win2012("2012");

	private String name;

	private WinType(String name) {
		this.name = name;

	}

	public String getName() {
		return name;
	}
}
