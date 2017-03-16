package ass1.packet.client;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import ass1.packet.encryption.Cryptor;
import ass1.packet.helper.App;

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
		int BUFFER_SIZE = 56 / 8;

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
			
			//@2017
			Cryptor cryptor = App.getDownloadingCryptor();
			byte[] plain;
			
			while ((count = bis.read(buf)) != -1) {
				
				//DECRYPT @2017
				plain = cryptor.decrypt(buf);
				//WRITE PLAIN_TEXT @2017
				os.write(plain, 0, plain.length);
				
				reads = reads + count;
				long p = (reads / (fileSize != 0 ? fileSize : 1)) / 11;
				clientWindow.updateStatus(row_index, p, "Downloading");
			}
			clientWindow.updateStatus(row_index, 100, "Complete");
			os.close();
			is.close();
			JOptionPane.showMessageDialog(null, "Download Complete !\nFile : " + fileName, "SUCCESS !", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Download Fail !", "ERROR !", JOptionPane.ERROR_MESSAGE);
		} 
	}

}
