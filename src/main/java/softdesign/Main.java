package main.java.softdesign;

public class Main {

	private static final long REPORT_INTERVAL = 5000L;

	private void start() throws InterruptedException {
		Environment environment = Environment.getInstance();
		CentralStation centralStation = CentralStation.getInstance();
		centralStation.startMission(environment);

		while (!centralStation.isMissionComplete()) {
			System.out.println(centralStation.getMissionProgress());
			Thread.sleep(REPORT_INTERVAL);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Main().start();
	}
}
