package cse.crypto.server;

import java.net.InetAddress;

public class ListDownload {
	private final int ID;
	public String fileName;
	public InetAddress address;
	public int port;
	public String string;
	private final int IDFile;
	
	public ListDownload(final int ID,String fileName, InetAddress address, int port, String string, final int IDFile) {
		this.ID = ID;
		this.fileName = fileName;
		this.address = address;
		this.port = port;
		this.string = string;
		this.IDFile = IDFile;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getfileName() {
		return fileName;
	}
	
	public String getString() {
		return string;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public int getIDFile() {
		return IDFile;
	}
}
