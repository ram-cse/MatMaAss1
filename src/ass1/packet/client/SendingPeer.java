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

import ass1.packet.encryption.Cryptor;
import ass1.packet.helper.App;
import ass1.packet.helper.Debug;

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
		
		final int BUFFER_SIZE = 64;
		BufferedInputStream bis = null;
		InputStream input = null;
		DataOutputStream output = null;

		
		try {
			Socket socket = serverSocket.accept();
			// initialize Streams
			output = new DataOutputStream(socket.getOutputStream());
			// write our username
			
			input = new FileInputStream(new File(files.getAbsolutePath()));
			bis = new BufferedInputStream(input);
			
			byte[] buf = new byte[BUFFER_SIZE];
			int count, read_bytes=0;
			
			Cryptor cryptor = App.getSendingCryptor();
			byte[] cypher;
			int  i = 0;
			while((count = bis.read(buf)) != -1){ i++;
				read_bytes = read_bytes + count;				
				//Encrypt
				cypher = cryptor.encrypt(buf);
				
				/*if(i % 5 == 0 || i == 1){
					Debug.d("cypherlen", cypher.length +"- buff:" + buf.length);
				}*/
				//Send Cypher
				output.write(cypher, 0, cypher.length); // write bytes to Output stream
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				output.flush();
				bis.close();
				input.close();
				output.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


