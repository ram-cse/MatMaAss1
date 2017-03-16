package ass1.packet.encryption;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import ass1.packet.encryption.rsa.RSAV1;
import ass1.packet.helper.Debug;
import ass1.packet.helper.FileUtils;

public class RSACryptor extends Cryptor{

/*	protected Key privateKey;
	protected Key publicKey;*/
	
	protected PublicKey publicKeyV1;
	protected PrivateKey privateKeyV1;
	
	private static final String PUBLIC_KEY_FILE_NAME =  FileUtils.ASSEST_DIR + "/rsa_public_key_v1.key";
	private static final String PRIVATE_KEY_FILE_NAME =  FileUtils.ASSEST_DIR + "/rsa_private_key_v1.key";

	
	public RSACryptor() {
		setUpKey();
	}
	
	private void setUpKey(){
		/*try {
			privateKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR + "/rsa_private_key.key");
			publicKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR + "/rsa_public_key.key");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		if(!FileUtils.isFileExist(PUBLIC_KEY_FILE_NAME)){
			RSAV1.generateKey(1024, PUBLIC_KEY_FILE_NAME, PRIVATE_KEY_FILE_NAME); // MAXIMUM DATA EN/DECYPT = 32 bytes
			Debug.d("setUpKey()", "Gen new Key");
		}
		
		try {
			publicKeyV1 = (PublicKey)FileUtils.readFileObject(PUBLIC_KEY_FILE_NAME);
			privateKeyV1 = (PrivateKey)FileUtils.readFileObject(PRIVATE_KEY_FILE_NAME);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		
	} 
	
	

	@Override
	public byte[] encrypt(byte[] plain) { // CHECK KEY NULL
		/*byte[] cypher = RSA.encrypt(plain, publicKey);*/
		byte[] cypher = RSAV1.encrypt(plain, publicKeyV1);
		return cypher;
	}

	@Override
	public byte[] decrypt(byte[] cypher) {// CHECK KEY NULL
		/*byte[] plain = RSA.decrypt(cypher, privateKey);*/
		byte[] plain = RSAV1.decrypt(cypher, privateKeyV1);
		return plain;
	}
	
	
/*	public int encyptFile(String filename, String newFilename){
		
		return 0;
	}
	
public int decyptFile(String filename, String newFilename){
		
		return 0;
	}*/
	

}
