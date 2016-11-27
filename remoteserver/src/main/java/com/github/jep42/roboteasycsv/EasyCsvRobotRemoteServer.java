package com.github.jep42.roboteasycsv;


import org.robotframework.remoteserver.RemoteServer;

public final class EasyCsvRobotRemoteServer {

	private static final String PATH = "/RobotEasyCsv";

	private static int port = 8270;

    private EasyCsvRobotRemoteServer() {
		super();
	}

	public static void main(String[] args) throws Exception {
    	parseArguments(args);

        org.robotframework.remoteserver.RemoteServer.configureLogging();
        RemoteServer server = new RemoteServer();
        server.setPort(port);
        server.putLibrary(PATH, new RobotEasyCsv());
        server.start();
    }

	private static void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if ("--port".equalsIgnoreCase(args[i])) {
				port = Integer.parseInt(args[i + 1]);
			}
		}
	}

}
