package cse.crypt.server;

import java.net.InetAddress;

public class ListClient {
	
	public String name;
	public InetAddress address;
	public int port;
	private final int ID;
	public int attempt = 0;
	
	public ListClient(String name, InetAddress address, int port, final int ID) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
	
}

