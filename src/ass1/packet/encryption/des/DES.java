package ass1.packet.encryption.des;

package DES;

import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;

public class DES {
	public static IvParameterSpec iv;
	public static SecretKey secretKey;
	/********************************************************************************************/
	/********************************************************************************************/
	/**************************************Initialize Vector (IV)********************************/
	public static IvParameterSpec CreateIV() throws NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		SecureRandom secureRandom = new SecureRandom();
		byte[] ivspec = new byte[cipher.getBlockSize()];
		secureRandom.nextBytes(ivspec);
		iv = new IvParameterSpec(ivspec);
		return iv;
	}
	
	/********************************************************************************************/
	/********************************************************************************************/
	/**************************************Initialize Secret KEY*********************************/
	public static SecretKey CreateKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		secretKey = kg.generateKey();
		return secretKey;
	}
	
	/********************************************************************************************/
	/********************************************************************************************/
	/******************************************Encrypt File**************************************/
	public static byte[] encrypt(byte[] File_Encrypt)
			throws Exception {
		 // create a binary key
		secretKey = CreateKey();
		
		// create an instance of cipher
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		// generate an initialization vector (IV)
		iv = CreateIV();
		
		// initialize the cipher with the key and IV
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		// enctypt!
		byte[] encrypted = cipher.doFinal(File_Encrypt);

		return encrypted;
	}

	/********************************************************************************************/
	/********************************************************************************************/
	/******************************************Decrypt File**************************************/
	/* @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException */
	public static byte[] decrypt(byte[] File_Decrypt) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// do the decryption with that key
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] decrypted = cipher.doFinal(File_Decrypt);

		return decrypted;
	}
}
