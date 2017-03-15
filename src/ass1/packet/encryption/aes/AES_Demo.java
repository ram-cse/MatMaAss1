package ass1.packet.encryption.aes;

import java.io.File;

public class AES_Demo {
  
    
    public static void main(String[] args) {
    	AES aes = new AES();
    	
        File inputFile = new File("Faded.mp4");
       // File decryptedFile = new File("Faded_decrypted.mp4");
         
        
        
        try {
			aes.encrypt(inputFile);
			aes.decrypt(aes.get_Key(),aes.get_Iv(), aes.get_pathFileEncrypted());
		} catch (CryptoException e) {
			e.printStackTrace();
		}
        
    }
}
