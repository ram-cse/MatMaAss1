package ass1.packet.client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.jar.Attributes.Name;

import javax.security.auth.login.FailedLoginException;
import javax.swing.JOptionPane;

public class ReceivingPeer extends Thread {
	
	private ClientWindow clientWindow;
	private String address;
	private int port;
	private long fileSize;
	private String pathFile;
	private String fileName;
	private final int row_index;
	
	public ReceivingPeer(String address, int port, long fileSize, String pathFile, String fileName, final int row_index, ClientWindow clientWindow) {
		this.address = address;
		this.port = port;
		this.fileSize = fileSize;
		this.pathFile = pathFile;
		this.fileName = fileName;
		this.row_index = row_index;
		this.clientWindow = clientWindow;
	}
	
	public void run() {
		Socket socket;
		DataOutputStream output;
		int BUFFER_SIZE = 1024;

		InputStream is = null;
		OutputStream os = null;
		
		try {
			socket = new Socket(address, port);
			// initialize streams
			output = new DataOutputStream(socket.getOutputStream());
			
			is = socket.getInputStream();
			os = new FileOutputStream(pathFile + "\\" + fileName);
			
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buf = new byte[BUFFER_SIZE];
			int count, reads = 0;
			while ((count = bis.read(buf)) != -1) {
				os.write(buf, 0, count);
				reads = reads + count;
				long p = (reads / fileSize) / 11;
				clientWindow.updateStatus(row_index, p, "Downloading");
			}
			clientWindow.updateStatus(row_index, 100, "Complete");
			os.close();
			is.close();
			JOptionPane.showMessageDialog(null, "Download Complete !\nFile : " + fileName, "SUCCESS !", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Download Fail !", "ERROR !", JOptionPane.ERROR_MESSAGE);
		} 
	}

}
