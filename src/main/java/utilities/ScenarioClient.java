
package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import basic.Client;
import basic.scenario.Scenario;
import basic.scenario.ScenarioIO;
import basic.scenario.ScenarioResult;

/**
 * the client used to execute a Scenario.java
 * 
 * @author zhangyutao
 * 
 * 
 */
public class ScenarioClient implements Client {

	public ScenarioClient() {

	}

	HashMap<Integer, ScenarioResult> result;

	/**
	 * Execute the given Scenario
	 * 
	 * @param scenario
	 *            the Scenario class which you want to execute.
	 * @param datainput
	 *            the data you would use to execute the scenario.
	 * @param iteration
	 *            the number how many times you would to repeate on the
	 *            scenario.the 1 means only run one time.
	 * @param isConcurrent
	 *            whether start the scenario concurrently.
	 * @param timeout
	 *            the timeout for execute one scenario.it is in milliseconds
	 * @return
	 * @author zhangyutao
	 */
	public HashMap<Integer, ScenarioResult> execute(Scenario scenario, ScenarioIO datainput, int iteration,
			boolean isConcurrent, int timeout) {
		if (timeout <= 0) {
			timeout = 180000;
		}
		ArrayList<Thread> iterationScenarioThreads = new ArrayList<Thread>();
		int iterationRunCount = 0;

		String scenarioName = scenario.getClass().getName();

		HashMap<Integer, ScenarioResult> outputs = new HashMap<Integer, ScenarioResult>();
		HashMap<Integer, IterationScenario> scenarioList = new HashMap<Integer, IterationScenario>();

		if (iteration < 1) {
			System.out.println("Scenario \"" + scenarioName + "\":iteration is less than 1, so skip it.");
		} else {
			for (int h = 1; h <= iteration; h++) {
				iterationRunCount = iterationRunCount + 1;
				String thrname = String.valueOf(iterationRunCount);

				IterationScenario iteraionScenario = new IterationScenario(scenario, datainput);
				Thread iterationScenarioThread = new Thread(iteraionScenario, thrname);

				iterationScenarioThreads.add(iterationScenarioThread);

				if (isConcurrent) {
					iterationScenarioThread.start();
					System.out.println(
							"Execute scenario \"" + scenarioName + "\"" + "(Iteration " + String.valueOf(h) + ")");
					scenarioList.put(h, iteraionScenario);

				} else {

					iterationScenarioThread.start();
					System.out.println(
							"Execute scenario \"" + scenarioName + "\"" + "(Iteration " + String.valueOf(h) + ")");
					try {
						iterationScenarioThread.join(timeout);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					outputs.put(h, new ScenarioResult(iteraionScenario.scenario.getStatus(), iteraionScenario.output));

					System.out.println(
							"Scenario \"" + scenarioName + "\"" + "(Iteration " + String.valueOf(h) + ") is end.");

				}

			}

			// wait for scenario thread end
			if (isConcurrent) {
				if (iterationScenarioThreads.size() > 0) {
					for (Thread thread : iterationScenarioThreads) {
						if (thread.isAlive()) {
							try {
								thread.join(timeout);// milliseconds
								System.out.println("Scenario \"" + scenarioName + "\"" + "(Iteration "
										+ String.valueOf(thread.getName()) + ") is end.");

							} catch (Throwable t) {
								t.printStackTrace();
							}
						}

					}
				}

				Iterator<Integer> iterator = scenarioList.keySet().iterator();
				while (iterator.hasNext()) {
					int index = iterator.next();
					IterationScenario iteraionScenario = scenarioList.get(index);

					outputs.put(index,
							new ScenarioResult(iteraionScenario.scenario.getStatus(), iteraionScenario.output));

				}

			}
		}

		return outputs;
	}

	private class IterationScenario implements Runnable {
		private Scenario scenario;
		private ScenarioIO output;

		public IterationScenario(Scenario scenario, ScenarioIO input) {
			this.scenario = scenario;
			this.scenario.setInput(input);
		}

		@Override
		public void run() {
			try {
				Thread.sleep(10);
				scenario.execute();
				this.output = scenario.getOutput();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, ScenarioResult> getResponse() throws Exception {
		// TODO Auto-generated method stub
		return this.result;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void execute(T content) throws Exception {
		// TODO Auto-generated method stub
		this.result = execute((Scenario) ((ArrayList<Object>) content).get(0),
				(ScenarioIO) ((ArrayList<Object>) content).get(1), (int) ((ArrayList<Object>) content).get(2),
				(boolean) ((ArrayList<Object>) content).get(3), (int) ((ArrayList<Object>) content).get(4));
	}
}
