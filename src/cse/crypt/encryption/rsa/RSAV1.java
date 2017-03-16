package cse.crypt.encryption.rsa;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import cse.crypt.helper.MyFileUtils;


public class RSAV1 {

  public static final String ALGORITHM = "RSA";
  Cipher cipher;
  
  boolean isInited = false; 
  public boolean isInited() {
	return isInited;
}
  
  public RSAV1() throws NoSuchAlgorithmException, NoSuchPaddingException {
	  init();
  }
  
  private void init() throws NoSuchAlgorithmException, NoSuchPaddingException {
		  cipher = Cipher.getInstance(ALGORITHM);
		  isInited = true;
  }
  
  
  public static void generateKey(int keySize, String publicKeyFileName, String privateKeyFileName) {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(keySize);
      final KeyPair key = keyGen.generateKeyPair(); // GENERATE KEY
      
      MyFileUtils.writeObject(key.getPublic(), publicKeyFileName);
      MyFileUtils.writeObject(key.getPrivate(), privateKeyFileName);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  
  public byte[] encrypt(byte[] data, PublicKey key) {
    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      // encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }

  public byte[] decrypt(byte[] data, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(data);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return dectyptedText;
  }
  

  /**
   * Test the EncryptionUtil
   */
  public static void main(String[] args) {
	  
	  String publicKeyFileName = MyFileUtils.DATA_DIR +"/rsa_public_key_v1.key";
	  String privateKeyFileName = MyFileUtils.DATA_DIR +"/rsa_private_key_v1.key";
	  
    try {
        generateKey(64 * 8, publicKeyFileName, privateKeyFileName); // 64 bytes

      final String originalText = "Text to be encrypted ";
      final PublicKey publicKey = (PublicKey) MyFileUtils.readFileObject(publicKeyFileName);
      
      RSAV1 rsav1 = new RSAV1();
      
      final byte[] cipherText = rsav1.encrypt(originalText.getBytes(), publicKey);

      // Decrypt the cipher text using the private key.
      final PrivateKey privateKey = (PrivateKey)MyFileUtils.readFileObject(privateKeyFileName);
      final byte[] plainText = rsav1.decrypt(cipherText, privateKey);

      // Printing the Original, Encrypted and Decrypted Text
      System.out.println("Original: " + originalText);
      System.out.println("Encrypted: " +new String(cipherText));
      System.out.println("Decrypted: " + new String(plainText));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}