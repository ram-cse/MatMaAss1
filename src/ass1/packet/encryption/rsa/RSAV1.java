package ass1.packet.encryption.rsa;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import ass1.packet.helper.FileUtils;


public class RSAV1 {

  public static final String ALGORITHM = "RSA";

  
  
  public static void generateKey(int keySize, String publicKeyFileName, String privateKeyFileName) {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(keySize);
      final KeyPair key = keyGen.generateKeyPair(); // GENERATE KEY

      File privateKeyFile = new File(privateKeyFileName);
      File publicKeyFile = new File(publicKeyFileName);

      // Create files to store public and private key
      if (privateKeyFile.getParentFile() != null) {
        privateKeyFile.getParentFile().mkdirs();
      }
      privateKeyFile.createNewFile();

      if (publicKeyFile.getParentFile() != null) {
        publicKeyFile.getParentFile().mkdirs();
      }
      publicKeyFile.createNewFile();

      // Saving the Public key in a file
      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
          new FileOutputStream(publicKeyFile));
      publicKeyOS.writeObject(key.getPublic());
      publicKeyOS.close();

      // Saving the Private key in a file
      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
          new FileOutputStream(privateKeyFile));
      privateKeyOS.writeObject(key.getPrivate());
      privateKeyOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  
  public static byte[] encrypt(byte[] data, PublicKey key) {
    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      // encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }

  public static byte[] decrypt(byte[] data, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM);

      // decrypt the text using the private key
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
	  
	  String publicKeyFileName = FileUtils.ASSEST_DIR +"/rsa_public_key_v1.key";
	  String privateKeyFileName = FileUtils.ASSEST_DIR +"/rsa_private_key_v1.key";
	  
    try {
        generateKey(1024, publicKeyFileName, privateKeyFileName); // 64 + 11 bytes

      final String originalText = "Text to be encrypted ";
      final PublicKey publicKey = (PublicKey) FileUtils.readFileObject(publicKeyFileName);
      final byte[] cipherText = encrypt(originalText.getBytes(), publicKey);

      // Decrypt the cipher text using the private key.
      final PrivateKey privateKey = (PrivateKey)FileUtils.readFileObject(privateKeyFileName);
      final byte[] plainText = decrypt(cipherText, privateKey);

      // Printing the Original, Encrypted and Decrypted Text
      System.out.println("Original: " + originalText);
      System.out.println("Encrypted: " +new String(cipherText));
      System.out.println("Decrypted: " + new String(plainText));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}