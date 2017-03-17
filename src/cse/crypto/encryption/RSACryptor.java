package cse.crypto.encryption;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.NoSuchPaddingException;

import cse.crypto.encryption.rsa.RSAV1;
import cse.crypto.helper.App;
import cse.crypto.helper.Debug;
import cse.crypto.helper.Utils;

public class RSACryptor extends Cryptor{

/*	protected Key privateKey;
	protected Key publicKey;*/
	
	protected PublicKey publicKeyV1;
	protected PrivateKey privateKeyV1;
	
	private static final String PUBLIC_KEY_FILE_NAME =  Utils.DATA_DIR + "/rsa_public_key_v1.key";
	private static final String PRIVATE_KEY_FILE_NAME =  Utils.DATA_DIR + "/rsa_private_key_v1.key";
	
	RSAV1 rsav1;
	
	
	public RSACryptor() {
		try {
			rsav1 = new RSAV1();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setUpKey();
	}
	
	private void setUpKey(){		
		if(!Utils.isFileExist(PUBLIC_KEY_FILE_NAME)){
			RSAV1.generateKey(App.RSA_KEY_LEN, PUBLIC_KEY_FILE_NAME, PRIVATE_KEY_FILE_NAME); // MAXIMUM DATA EN/DECYPT = 32 bytes
			Debug.d("setUpKey()", "Gen new Key");
		}
		
		try {
			publicKeyV1 = (PublicKey)Utils.readFileObject(PUBLIC_KEY_FILE_NAME);
			privateKeyV1 = (PrivateKey)Utils.readFileObject(PRIVATE_KEY_FILE_NAME);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		
	} 
	
	

	@Override
	public byte[] encrypt(byte[] plain) { // CHECK KEY NULL
		byte[] cypher = rsav1.encrypt(plain, publicKeyV1);
		return cypher;
	}

	@Override
	public byte[] decrypt(byte[] cypher) {// CHECK KEY NULL
		byte[] plain = rsav1.decrypt(cypher, privateKeyV1);
		return plain;
	}
	
	/*try {
	privateKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR + "/rsa_private_key.key");
	publicKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR + "/rsa_public_key.key");
} catch (IOException e) {
	e.printStackTrace();
}*/
	
	public PublicKey getPublicKey() {
		return publicKeyV1;
	}
	
	public PrivateKey getPrivateKey() {
		return privateKeyV1;
	}
	

}
