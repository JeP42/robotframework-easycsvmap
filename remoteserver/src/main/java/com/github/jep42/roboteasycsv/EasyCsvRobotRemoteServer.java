package com.github.jep42.roboteasycsv;


import org.robotframework.remoteserver.RemoteServer;

public class EasyCsvRobotRemoteServer {

	private static final String PATH = "/EasyCSVLibrary";

	private static int PORT = 8270;


    public static void main(String[] args) throws Exception {
    	parseArguments(args);

        org.robotframework.remoteserver.RemoteServer.configureLogging();
        RemoteServer server = new RemoteServer();
        server.setPort(PORT);
        server.putLibrary(PATH, new RobotEasyCsv());
        server.start();
    }

	private static void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("--port")) {
				PORT = Integer.parseInt(args[i + 1]);
			}
		}
	}

}
