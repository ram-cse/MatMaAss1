package cse.crypto.encryption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import cse.crypto.encryption.daes.CryptorKeys;
import cse.crypto.encryption.daes.DAES;
import cse.crypto.helper.App;
import cse.crypto.helper.Utils;
import cse.crypto.helper.App.AlgType;

public class DAESCryptor extends Cryptor {
	
	DAES aes;
	CryptorKeys keys;
	
	private AlgType algType;
	
	private static final String AES_KEY_FILE = Utils.DATA_DIR + "/aes_key.key";
	private static final String AES_IV_FILE = Utils.DATA_DIR + "/aes_iv.key";
	
	private static final String DES_KEY_FILE = Utils.DATA_DIR + "/des_key.key";
	private static final String DES_IV_FILE = Utils.DATA_DIR + "/des_iv.key";
	
	public DAESCryptor(AlgType type) {
		this.algType = type;
		try {
			setUpKey();
			aes = new DAES(keys, type == AlgType.AES? DAES.AES_ALG : DAES.DES_ALG );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] encrypt(byte[] plain) throws Exception{ // CHECK IS INIT SUCCESSFULLY
		return aes.encrypt(plain);
	}

	@Override
	public byte[] decrypt(byte[] cypher) throws Exception{
		return aes.decrypt(cypher);
	}
	
	//======================================== SET UP KEY============================================//
	
	private void setUpKey() throws Exception{
		/*if(!Utils.isFileExist(KEY_FILE)){
			// CREATE NEW KEY
			AesKey aesKey = AES.generateKey(KEY_BIT_LEN);
				FileUtils.writeObject(aesKey, KEY_FILE);
			storeKey(aesKey);
		}*/
		if(algType == AlgType.AES){
			 keys = readKey(AES_KEY_FILE, AES_IV_FILE);
		}else{
			 keys = readKey(DES_KEY_FILE, DES_IV_FILE);
		}		
	}
	
	public static void storeKey(CryptorKeys aesKey, String keyFilePath, String ivFilePath) throws IOException{
		Utils.writeBytes(keyFilePath, aesKey.getKey());
		Utils.writeBytes(ivFilePath, aesKey.getIv());
	}
	
	
	public static CryptorKeys readKey(String keyFilePath, String ivFilePath) throws IOException{
		CryptorKeys aesKey = new CryptorKeys();
		aesKey.setKey(Files.readAllBytes(Paths.get(keyFilePath)));
		aesKey.setIv(Files.readAllBytes(Paths.get(ivFilePath)));
		return aesKey;
	}
	
	public static void main(String[] args) {
		try {
			CryptorKeys aesKey = DAES.generateKey(App.AES_KEY_LEN, AlgType.AES);
			storeKey(aesKey, AES_KEY_FILE, AES_IV_FILE);
			
			aesKey = DAES.generateKey(App.DES_KEY_LEN, AlgType.DES);
			storeKey(aesKey, DES_KEY_FILE, DES_IV_FILE);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
