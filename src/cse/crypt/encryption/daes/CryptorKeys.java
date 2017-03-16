package cse.crypt.encryption.daes;

import java.io.Serializable;

public class CryptorKeys implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte[] key;
	private byte[] iv;
	public byte[] getKey() {
		return key;
	}
	public void setKey(byte[] key) {
		this.key = key;
	}
	public byte[] getIv() {
		return iv;
	}
	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	
	
	

}
