package DES;

import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;

public class DES {
	/********************************************************************************************/
	/********************************************************************************************/
	/**************************************Initialize Vector (IV)********************************/
	public static IvParameterSpec CreateIV() throws NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		SecureRandom secureRandom = new SecureRandom();
		byte[] ivspec = new byte[cipher.getBlockSize()];
		secureRandom.nextBytes(ivspec);
		IvParameterSpec iv = new IvParameterSpec(ivspec);
		return iv;
	}
	
	/********************************************************************************************/
	/********************************************************************************************/
	/**************************************Initialize Secret KEY*********************************/
	public static SecretKey CreateKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		SecretKey sk = kg.generateKey();
		return sk;
	}
	
	/********************************************************************************************/
	/********************************************************************************************/
	/******************************************Encrypt File**************************************/
	public static byte[] encrypt(byte[] File_Encrypt /* String toEncrypt */, SecretKey secretKey, IvParameterSpec iv)
			throws Exception {
		// create an instance of cipher
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		// initialize the cipher with the key and IV
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		// enctypt!
		byte[] encrypted = cipher.doFinal(File_Encrypt);

		return encrypted;
	}

	/********************************************************************************************/
	/********************************************************************************************/
	/******************************************Decrypt File**************************************/
	public static byte[] decrypt(byte[] File_Decrypt, SecretKey secretKey, IvParameterSpec iv) throws Exception {
		// do the decryption with that key
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] decrypted = cipher.doFinal(File_Decrypt);

		return decrypted;
	}
}
