package cse.crypto.client;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import cse.crypto.encryption.Cryptor;
import cse.crypto.helper.App;
import cse.crypto.helper.App.AlgType;
import cse.crypto.helper.Debug;
import cse.crypto.helper.Utils;

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
		DataInputStream is = null;
		OutputStream os = null;
		int bufferSize = 0;
		int len = 0;
		int reads = 0;
		byte[] plain = null;
		Cryptor cryptor;
		try {
			socket = new Socket(address, port);
			// initialize streams			
			is = new DataInputStream(socket.getInputStream());
			os = new FileOutputStream(pathFile + "\\" + fileName);
						
			//@2017
			
			char c;
			StringBuffer algName = new StringBuffer(); 
			while((c = is.readChar()) != '/'){
				algName.append(c);
			}
			AlgType algType = AlgType.valueOf(algName.toString());
			Debug.d("ALG:" + algType.getName());
			if(algType == AlgType.RSA){
				bufferSize = App.RSA_KEY_LEN / 8;
			} else if(algType == AlgType.AES){
				bufferSize = App.AES_SENDING_BUFFER + App.AES_IV_LEN;
			}else if(algType == AlgType.DES){
				bufferSize = App.DES_SENDING_BUFFER + App.DES_IV_LEN;
			}
			cryptor = App.getCryptor(algType);
			byte[] buf = new byte[bufferSize];
			
			while ((len = is.read(buf)) > 0) {
				if(len < bufferSize){ // trim bytes
					buf = Utils.trim(buf, len);
				}
				//Debug.d("CP: " + len);
				//DECRYPT @2017
				plain = cryptor.decrypt(buf); // DATA == BUFF?
				//WRITE PLAIN_TEXT @2017
				os.write(plain, 0, plain.length);
				reads += plain.length;
				long p = (reads / (fileSize != 0 ? fileSize : 1)) / 11;
				clientWindow.updateStatus(row_index, p, "Downloading");
			}
			clientWindow.updateStatus(row_index, 100, "Complete");
			JOptionPane.showMessageDialog(null, "Download Complete !\nFile : " + fileName, "SUCCESS !", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Download Fail !", "ERROR !", JOptionPane.ERROR_MESSAGE);
		}finally {
			try{
				os.flush();
				os.close();
				is.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
	
/*	private String readChars(DataInputStream in, int endCode) throws IOException{
		int ch;
		StringBuffer buffer = new StringBuffer();
		while((ch = in.readInt()) != endCode){
			buffer.append(ch);
		}
		return buffer.toString();
	}*/
		
}
