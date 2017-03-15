package ass1.packet.helper;

import ass1.packet.encryption.AESCryptor;
import ass1.packet.encryption.Cryptor;
import ass1.packet.encryption.DESCryptor;
import ass1.packet.encryption.RSACryptor;

public class App {
	
	private static AlgType sendAlgName = AlgType.RSA;
	private static AlgType downloadAlgName = AlgType.RSA;

	
	public enum AlgType{
		RSA,
		DES,
		AES
	}
	
	public static AlgType getSendingAlgName() {
		return sendAlgName;
	}
	
	public static void setSendingAlgName(AlgType algName) {
		sendAlgName = algName;
	}
	
	public static void setDownloadingAlgName(AlgType receiveAlgName) {
		downloadAlgName = receiveAlgName;
	}
	
	public static AlgType getDownloadingAlgName() {
		return downloadAlgName;
	}
	
	public static Cryptor getSendingCryptor(){
		Cryptor cryptor = null;
		switch (sendAlgName) {
		case RSA:
			cryptor = new RSACryptor();
			break;
		case DES:
			cryptor = new DESCryptor();
			break;
		case AES:
			cryptor = new AESCryptor();
			break;
		default:
			cryptor = new RSACryptor();
			Debug.d("getSendingCryptor", "sw default");
			break;
		}
		
		return cryptor;
	}
	
	public static Cryptor getDownloadingCryptor(){
		Cryptor cryptor = null;
		switch (downloadAlgName) {
		case RSA:
			cryptor = new RSACryptor();
			break;
		case DES:
			cryptor = new DESCryptor();
			break;
		case AES:
			cryptor = new AESCryptor();
			break;
		default:
			cryptor = new RSACryptor();
			Debug.d("getSendingCryptor", "sw default");
			break;
		}
		
		return cryptor;
	}
	

}
