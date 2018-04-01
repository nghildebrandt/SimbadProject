package main.java.softdesign;

public class Main {

	private static final long REPORT_INTERVAL = 2000L;

	private void start() throws InterruptedException {
		Environment environment = Environment.getInstance();
		CentralStation centralStation = CentralStation.getInstance();
		centralStation.startMission(environment);

		while (!centralStation.isMissionComplete()) {
			centralStation.reportMissionProgress();
			Thread.sleep(REPORT_INTERVAL);
		}

		centralStation.endMission();
	}

	public static void main(String[] args) throws InterruptedException {
		new Main().start();
	}
}
