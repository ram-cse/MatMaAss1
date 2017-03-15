package ass1.packet.encryption.aes;

public class CryptoException extends Exception {
	 
    public CryptoException() {
    }
 
    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}