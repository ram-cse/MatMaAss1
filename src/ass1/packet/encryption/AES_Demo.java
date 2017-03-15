package ass1.packet.encryption;

import java.io.File;

public class AES_Demo {
  
    
    public static void main(String[] args) {
    	AES aes = new AES();
    	
        File inputFile = new File("Faded.mp4");
        File encryptedFile = new File("Faded_decrypted.mp4");
        File decryptedFile = new File("Faded_decrypted.mp4");
         
        try {
			aes.encrypt(inputFile, encryptedFile);
			aes.decrypt(aes.getKey(),aes.getIv(), decryptedFile);
		} catch (CryptoException e) {
			e.printStackTrace();
		}
        
    }
}
