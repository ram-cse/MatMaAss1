package ass1.packet.client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.management.loading.PrivateClassLoader;

public class SendingPeer extends Thread{
	
	private Files files;
	private int port;
	private ServerSocket serverSocket;
	
	public SendingPeer(ServerSocket serverSocket, String pathfile) {
		this.files = new Files(pathfile);
		//this.port = port;
		this.serverSocket = serverSocket;
	}

	
	public void run() {		
		DataOutputStream output;
		
		final int BUFFER_SIZE = 1024;
		
		try {
			Socket socket = serverSocket.accept();
			// initialize Streams
			output = new DataOutputStream(socket.getOutputStream());
			// write our username
			
			InputStream input = new FileInputStream(new File(files.getAbsolutePath()));
			BufferedInputStream bis = new BufferedInputStream(input);
			
			byte[] buf = new byte[BUFFER_SIZE];
			int count, read_bytes=0;
			
			while((count = bis.read(buf)) != -1){
				read_bytes = read_bytes + count;
				long p = (read_bytes / files.getSize()) / 11;
				output.write(buf, 0, count); // write bytes to Output stream
			}
			
			output.flush();
			bis.close();
			input.close();
			output.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


