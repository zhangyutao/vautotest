package basic.scenario;

public class ScenarioResult {
	private ScenarioStatus ss;
	private ScenarioIO opt;

	public ScenarioStatus getStatus() {
		return ss;
	}

	public void setStatus(ScenarioStatus ss) {
		this.ss = ss;
	}

	public ScenarioIO getOutput() {
		return opt;
	}

	public void setOutput(ScenarioIO opt) {
		this.opt = opt;
	}

	public ScenarioResult(ScenarioStatus ss, ScenarioIO opt) {
		this.ss = ss;
		this.opt = opt;
	}

}
