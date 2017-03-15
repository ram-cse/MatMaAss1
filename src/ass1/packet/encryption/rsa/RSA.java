package ass1.packet.encryption.rsa;

import java.io.IOException;
import java.math.BigInteger;

import ass1.packet.utils.FileUtils;

/**
 * @author vrams
 *
 */
public class RSA {

	/** Encrypt the given plaintext message. */
	public static String encrypt(byte[] message, Key publicKey) {
		return (new BigInteger(message)).modPow(publicKey.getKey(), publicKey.getN()).toString();
	}

	/** Encrypt the given plaintext message. */
	public static BigInteger encrypt(BigInteger message, Key publicKey) {
		return message.modPow(publicKey.getKey(), publicKey.getN());
	}

	/** Decrypt the given ciphertext message. */
	public static byte[] decrypt(byte[] message, Key privateKey) {
		return (new BigInteger(message)).modPow(privateKey.getKey(), privateKey.getN()).toByteArray();
	}

	/** Decrypt the given ciphertext message. */
	public static BigInteger decrypt(BigInteger message, Key privateKey) {
		return message.modPow(privateKey.getKey(), privateKey.getN());
	}

	public static void main(String[] args) {
		try {
			KeyGenerator key = new KeyGenerator();
			key.generateKey();
			key.savePrivateKey(FileUtils.ASSEST_DIR+"/rsa_private_key.key");
			key.savePublicKey(FileUtils.ASSEST_DIR+"/rsa_public_key.key");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Key publicKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR+"/rsa_private_key.key");
			Key privateKey = KeyGenerator.readKeyFromFile(FileUtils.ASSEST_DIR+"/rsa_public_key.key");		
			
			String text1 = "Yellow and Black Border Collies Vơ Ram Điểu";
			System.out.println("Plaintext: " + text1);
			BigInteger plaintext = new BigInteger(text1.getBytes());

			BigInteger ciphertext = RSA.encrypt(plaintext, publicKey);
			System.out.println("Ciphertext: " + ciphertext);
			plaintext = RSA.decrypt(ciphertext, privateKey);

			String text2 = new String(plaintext.toByteArray());
			System.out.println("Plaintext: " + text2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
