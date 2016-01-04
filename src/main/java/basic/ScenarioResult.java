package basic;

public class ScenarioResult {
	private ScenarioStatus ss;
	private ScenarioIO opt;

	public ScenarioStatus getSs() {
		return ss;
	}

	public void setSs(ScenarioStatus ss) {
		this.ss = ss;
	}

	public ScenarioIO getOpt() {
		return opt;
	}

	public void setOpt(ScenarioIO opt) {
		this.opt = opt;
	}

	public ScenarioResult(ScenarioStatus ss, ScenarioIO opt) {
		this.ss = ss;
		this.opt = opt;
	}

}
