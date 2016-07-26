package com.epages.wiremock.starter;

import java.io.IOException;
import java.net.ServerSocket;

class InetUtils {

	public static int getFreeServerPort() {
		try (ServerSocket serverSocket = new ServerSocket(0)) {
			return serverSocket.getLocalPort();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
