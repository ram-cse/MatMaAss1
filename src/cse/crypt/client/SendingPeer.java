package cse.crypt.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cse.crypt.encryption.Cryptor;
import cse.crypt.helper.App;

public class SendingPeer extends Thread{
	
	private Files files;
	private ServerSocket serverSocket;
	
	public static final int SENDING_BUFFER_SIZE = 32;

	
	public SendingPeer(ServerSocket serverSocket, String pathfile) {
		this.files = new Files(pathfile);
		//this.port = port;
		this.serverSocket = serverSocket;
	}

	
	public void run() {		
		
	//	BufferedInputStream bis = null;
		InputStream input = null;
		DataOutputStream output = null;

		
		try {
			Socket socket = serverSocket.accept();
			// initialize Streams
			output = new DataOutputStream(socket.getOutputStream());
			// write our username
			input = new FileInputStream(new File(files.getAbsolutePath()));
/*			bis = new BufferedInputStream(input);
*/			
			byte[] buf = new byte[SENDING_BUFFER_SIZE];
			int len, read_bytes=0;
			
			Cryptor cryptor = App.getSendingCryptor();
			byte[] cypher;
			while((len = input.read(buf)) > 0){
				read_bytes = read_bytes + len;				
				if(len < SENDING_BUFFER_SIZE){ // trim bytes
					 byte[] smallerData = new byte[len];
			         System.arraycopy(buf, 0, smallerData, 0, len);
			         buf = smallerData;
				}
				//Encrypt
				cypher = cryptor.encrypt(buf); // PLAIN DAY BUFF?
				//Send Cypher
				output.write(cypher, 0, cypher.length); // write bytes to Output stream
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//output.flush();
		//		bis.close();
				input.close();
				output.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
}


