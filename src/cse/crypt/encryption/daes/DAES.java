
package cse.crypt.encryption.daes;

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

import cse.crypt.helper.App.AlgType;

public class DAES {

	Cipher cipher;
	Key secretKey;
	IvParameterSpec iv;
	
	CryptorKeys key;
	
	boolean isInited = false;
	
	private String algName;
	public String getAlgName() {
		return algName;
	}
	
	public static final String DES_ALG = "DES";
	public static final String AES_ALG = "AES";

	public DAES(CryptorKeys key, String algName) throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.algName = algName;
		this.key = key;
		iv = new IvParameterSpec(key.getIv());
		secretKey = new SecretKeySpec(key.getKey(), algName);
		cipher = Cipher.getInstance(algName + "/CBC/PKCS5PADDING");
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
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		output = cipher.doFinal(data);
		return output;
	}

	/*
	 * Auto Key
	 */
	public static CryptorKeys generateKey(int bitlen, AlgType type) {
		CryptorKeys output = new CryptorKeys();
		KeyGenerator gen = null;

		try {
			gen = KeyGenerator.getInstance(type == AlgType.AES ? "AES" : "DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		gen.init(bitlen);
		SecretKey secret = gen.generateKey();
		byte[] key = secret.getEncoded();
		
		output.setKey(key);
		output.setIv(generateIv(type));
		return output;
	}

	/*
	 * Auto IV
	 */
	private static byte[] generateIv(AlgType algType) {
		SecureRandom random = new SecureRandom();
		byte[] ivBytes = new byte[algType == AlgType.AES ? 16 : 8];
		random.nextBytes(ivBytes);
		return ivBytes;
	}

}