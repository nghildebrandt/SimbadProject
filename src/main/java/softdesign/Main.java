package main.java.softdesign;

public class Main {

	private void start() {
		Environment environment = Environment.getInstance();
		CentralStation centralStation = CentralStation.getInstance();
		centralStation.startMission(environment);
	}

	public static void main(String[] args) {
		new Main().start();
	}
}
