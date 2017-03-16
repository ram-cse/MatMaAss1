
package ass1.packet.encryption.aes;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	Cipher cipher;
	Key secretKey;
	IvParameterSpec iv;
	
	AesKey key;
	
	boolean isInited = false;

	public void init(AesKey key) throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.key = key;
		iv = new IvParameterSpec(key.getIv());
		secretKey = new SecretKeySpec(key.getKey(), "AES");
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		isInited = true;
	}
	
	public boolean isInited() {
		return isInited;
	}

	/*
	 * Encrypt
	 * 
	 */
	public byte[] encrypt(byte[] data) throws  InvalidKeyException, IllegalBlockSizeException,
	BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		byte[] output = null;
		/*iv = new IvParameterSpec(key.getIv());
		secretKey = new SecretKeySpec(key.getKey(), "AES");*/
		 cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		output = cipher.doFinal(data);
		return output;
	}

	/*
	 * Decrypt
	 * 
	 */
	public byte[] decrypt(byte[] data) throws InvalidKeyException, IllegalBlockSizeException,
	BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		byte[] output = null;
		/*iv = new IvParameterSpec(key.getIv());
		secretKey = new SecretKeySpec(key.getKey(), "AES");*/
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		output = cipher.doFinal(data);
		return output;
	}

	/*
	 * Do Crypto
	 
	private byte[] doCrypto(int cipherMode, byte[] data) throws CryptoException, InvalidAlgorithmParameterException {
		byte[] output = null;
		try {
			cipher.init(cipherMode, secretKey, iv);
			output = cipher.doFinal(data);
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			throw new CryptoException("Error encrypting/decrypting file", e);
		}

		return output;
	}*/

	/*
	 * Auto Key
	 */
	public static AesKey generateKey(int bitlen) {
		AesKey output = new AesKey();
		KeyGenerator gen = null;

		try {
			gen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		gen.init(bitlen);
		SecretKey secret = gen.generateKey();
		byte[] key = secret.getEncoded();
		
		output.setKey(key);
		output.setIv(generateIv());
		return output;
	}

	/*
	 * Auto IV
	 */
	private static byte[] generateIv() {
		SecureRandom random = new SecureRandom();

		byte[] ivBytes = new byte[16];
		random.nextBytes(ivBytes);

		return ivBytes;
	}

}