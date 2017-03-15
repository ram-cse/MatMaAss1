package test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import ass1.packet.helper.Debug;
import ass1.packet.helper.FileUtils;

public class Test {
	
	public static void main(String[] args) {
		/*String txt;
		try {
			txt = FileUtils.readPlainFile(FileUtils.ASSEST_DIR+ "/rsa_private_key.key");
			Debug.d("TEST", txt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public PrivateKey getPrivateKey(String filename) throws Exception {

	    File f = new File(filename);
	    FileInputStream fis = new FileInputStream(f);
	    DataInputStream dis = new DataInputStream(fis);
	    byte[] keyBytes = new byte[(int) f.length()];
	    dis.readFully(keyBytes);
	    dis.close();
	    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
	    KeyFactory kf =
	            KeyFactory.getInstance("RSA");
	    return kf.generatePrivate(spec);
	}

}
