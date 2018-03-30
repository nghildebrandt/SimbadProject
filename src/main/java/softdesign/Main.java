package main.java.softdesign;

public class Main {

	private static final long REPORT_INTERVAL = 500L;

	int stepCounter = 0;

	private void start() throws InterruptedException {
		Environment environment = Environment.getInstance();
		CentralStation centralStation = CentralStation.getInstance();
		centralStation.startMission(environment);

		while (!centralStation.isMissionComplete()) {
			stepCounter++;

			System.out.println(centralStation.getMissionProgress());

			Thread.sleep(REPORT_INTERVAL);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Main().start();
	}
}
