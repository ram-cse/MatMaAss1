package test;

import java.io.File;

import cse.crypt.encryption.daes.CryptoException;

public class AES_Demo {
  
    
    public static void main(String[] args) {
    	AES aes = new AES();
    	
        File inputFile = new File("D:\\ajajja.mp4");
      //  File encryptedFile = new File("D:\\tes.mp4");
        File decryptedFile = new File("D:\\ajajja_1.mp4");
         
        try {
			aes.encrypt(inputFile);
			aes.decrypt(aes.get_Key(),aes.get_Iv(), decryptedFile);
		} catch (CryptoException e) {
			e.printStackTrace();
		}
        
    }
}