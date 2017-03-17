package test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import cse.crypto.encryption.DAESCryptor;
import cse.crypto.helper.Debug;
import cse.crypto.helper.App.AlgType;

public class Test {
	
	public static void main(String[] args) {
		testAES();
	}
	
	private static void testAES(){
		String plainText = "Hello Ram Điểu";
		Debug.d("PlainText", plainText);
		DAESCryptor aes = new DAESCryptor(AlgType.DES);
		try {
			byte[] cp = aes.encrypt(plainText.getBytes());
			Debug.d("CP", new String(cp));
			
			byte[] pl = aes.decrypt(cp);
			Debug.d("DECYPT", new String(pl));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
