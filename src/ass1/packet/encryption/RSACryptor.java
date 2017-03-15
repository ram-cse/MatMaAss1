package ass1.packet.encryption;

import java.io.IOException;

import ass1.packet.encryption.rsa.Key;
import ass1.packet.encryption.rsa.KeyGenerator;
import ass1.packet.encryption.rsa.RSA;
import ass1.packet.helper.FileUtils;

public class RSACryptor extends Cryptor{

	protected Key privateKey;
	protected Key publicKey;
	
	public RSACryptor() {
		setUpKey();
	}
	
	private void setUpKey(){
		try {
			privateKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR + "/rsa_private_key.key");
			publicKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR + "/rsa_public_key.key");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	

	@Override
	public byte[] encrypt(byte[] plain) { // CHECK KEY NULL
		byte[] cypher = RSA.encrypt(plain, publicKey);
		return cypher;
	}

	@Override
	public byte[] decrypt(byte[] cypher) {// CHECK KEY NULL
		byte[] plain = RSA.decrypt(cypher, privateKey);
		return plain;
	}
	

}
