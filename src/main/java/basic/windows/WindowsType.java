package basic.windows;

public enum WindowsType {
	win2008("2008"), win2012("2012");

	private String name;

	private WindowsType(String name) {
		this.name = name;

	}

	public String getName() {
		return name;
	}
}
