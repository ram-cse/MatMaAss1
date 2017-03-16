package cse.crypt.client;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import cse.crypt.encryption.Cryptor;
import cse.crypt.encryption.DAESCryptor;
import cse.crypt.encryption.RSACryptor;
import cse.crypt.helper.App;
import cse.crypt.helper.Debug;

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
		InputStream is = null;
		OutputStream os = null;
		
		try {
			socket = new Socket(address, port);
			// initialize streams			
			is = socket.getInputStream();
			os = new FileOutputStream(pathFile + "\\" + fileName);
						
			//@2017
			Cryptor cryptor = App.getDownloadingCryptor();
			int buff_size = 64;  // == RSA KEY
			if(cryptor instanceof RSACryptor){
				buff_size = App.RSA_KEY_LEN / 8;
				Debug.d("BUFF, RSA");
			} else if(cryptor instanceof DAESCryptor){
				buff_size = SendingPeer.SENDING_BUFFER_SIZE + App.AES_KEY_LEN / 8;
				Debug.d("BUFF, DAES:" + App.getDownloadingAlgName() );
			}
			
			byte[] plain = null;
			byte[] buf = new byte[buff_size];
			int len, reads = 0;
			
			while ((len = is.read(buf)) > 0) {
				
				if(len < buff_size){ // trim bytes
					 byte[] smallerData = new byte[len];
			         System.arraycopy(buf, 0, smallerData, 0, len);
			         buf = smallerData;
				}
				
				//DECRYPT @2017
				plain = cryptor.decrypt(buf); // DATA == BUFF?
				//WRITE PLAIN_TEXT @2017
				os.write(plain, 0, plain.length);
				reads += plain.length;
				long p = (reads / (fileSize != 0 ? fileSize : 1)) / 11;
				clientWindow.updateStatus(row_index, p, "Downloading");
			}
			clientWindow.updateStatus(row_index, 100, "Complete");
			//os.flush();
			os.close();
			is.close();
			JOptionPane.showMessageDialog(null, "Download Complete !\nFile : " + fileName, "SUCCESS !", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Download Fail !", "ERROR !", JOptionPane.ERROR_MESSAGE);
		} 
	}

}
