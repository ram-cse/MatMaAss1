package cse.crypto.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cse.crypto.encryption.Cryptor;
import cse.crypto.helper.App;
import cse.crypto.helper.App.AlgType;
import cse.crypto.helper.Utils;

public class SendingPeer extends Thread{
	
	private Files files;
	private ServerSocket serverSocket;
	
	
	public SendingPeer(ServerSocket serverSocket, String pathfile) {
		this.files = new Files(pathfile);
		//this.port = port;
		this.serverSocket = serverSocket;
	}

	
	public void run() {		
		
		InputStream input = null;
		DataOutputStream output = null;
		int bufferSize = -1;
		int len = 0;
		int read_bytes=0;
		byte[] buf = null;
		byte[] cypher;
		Cryptor cryptor;

		try {
			Socket socket = serverSocket.accept();
			// initialize Streams
			output = new DataOutputStream(socket.getOutputStream());
			// write our username
			input = new FileInputStream(new File(files.getAbsolutePath()));
		
			
			AlgType type = App.getSendingAlgName();
			if(type == AlgType.RSA){
				bufferSize = App.RSA_SENDING_BUFFER;
			}else if(type == AlgType.DES){
				bufferSize = App.DES_SENDING_BUFFER;
			}else if(type == AlgType.AES){
				bufferSize = App.AES_SENDING_BUFFER;
			}else{
				throw new RuntimeException("Unknown AlgType");
			}
			
			buf = new byte[bufferSize];  
			cryptor = App.getCryptor(type);
			
			output.writeChars(type.toString());
			output.writeChar('/');
			
			/*if(type == AlgType.RSA){
				RSACryptor rsa = (RSACryptor) cryptor;
				output.writeChars(String.valueOf(rsa.getPublicKey().getEncoded())); // CHECK KEY contain value -1000001 '/'
				output.writeInt(-1000001);;
				output.writeChars(String.valueOf(rsa.getPrivateKey().getEncoded()));
				output.writeInt(-1000001);
			}else{
				DAESCryptor rsa = (DAESCryptor) cryptor;
				output.writeChars(String.valueOf(rsa.getKeys().getKey())); // CHECK KEY value -1000001 '/'
				output.writeInt(-1000001);;
				output.writeChars(String.valueOf(rsa.getKeys().getIv()));
				output.writeInt(-1000001);
			}*/
			
			while((len = input.read(buf)) > 0){
				read_bytes = read_bytes + len;				
				if(len < bufferSize){ // trim bytes
					 buf = Utils.trim(buf, len);
				}
				//Encrypt
				cypher = cryptor.encrypt(buf); 
				//Send Cypher
//				Debug.d("Plain:" + len + "_Cp:" + cypher.length);
				output.write(cypher, 0, cypher.length); // write bytes to Output stream
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				output.flush();
				input.close();
				output.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
}


