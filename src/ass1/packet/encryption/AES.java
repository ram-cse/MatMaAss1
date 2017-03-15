
package ass1.packet.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
	private byte[] _key;
	private byte[] _initVector;
	
	
	/*
	 * Encrypt
	 * 
	 */
    public void encrypt (File inputFile, File outputFile) throws CryptoException {
    	this._key = this.generateKey();
    	this._initVector = this.generateIv();
    	
        try {
			doCrypto(Cipher.ENCRYPT_MODE, _key, _initVector, inputFile, outputFile);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
    }
 
    
    /*
	 * Decrypt
	 * 
	 */
    public void decrypt (byte[] key, byte[] initVector, File inputFile) throws CryptoException {
        try {
			doCrypto(Cipher.DECRYPT_MODE, key, initVector, inputFile, inputFile);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
    }
 
    /*
     * Do Crypto
     */
    private void doCrypto(int cipherMode, byte[] key, byte[] initVector, File inputFile, File outputFile) throws CryptoException, InvalidAlgorithmParameterException {
        try {
        	IvParameterSpec iv = new IvParameterSpec(initVector);
            Key secretKey = new SecretKeySpec(key, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(cipherMode, secretKey, iv);
             
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
             
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();
             
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
    
    /*
     *  Auto Key
     */
    private byte[] generateKey () {
    	KeyGenerator gen = null;
    	
		try {
			gen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
        gen.init(128); /* 128-bit AES */
        SecretKey secret = gen.generateKey();
        
        byte[] key = secret.getEncoded();
        
        return key;
    }
    
    /*
     * Auto IV
     */
    private byte[] generateIv() {
        SecureRandom random = new SecureRandom();
        
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        
        return ivBytes;
    }
    
    
    /*
     *  Get Key
     */
    public byte[] getKey () {
    	return this._key;
    }
    
    /*
     *  Get IV
     */
    public byte[] getIv () {
    	return this._initVector;
    }
    
   
}