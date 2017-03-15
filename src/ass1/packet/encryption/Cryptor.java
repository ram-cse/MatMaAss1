package ass1.packet.encryption;

public abstract class Cryptor {
	
/*	*//**
	 * Store Key
	 *//*
	protected byte[] key;*/
	
	
	/**
	 * @param plain
	 * @return cipher
	 */
	public abstract byte[] encrypt(byte[] plain);

	/**
	 * @param cypher
	 * @return plain text
	 * 
	 * Decrypt cipher text
	 */
	public abstract byte[] decrypt(byte[] cypher);
	
/*	*//**
	 * Generate {@link #key} randomly and store to {@link #key}
	 *//*
	public abstract void generateKey();

	
	*//**
	 * 
	 * @return {@link #key}
	 *//*
	public byte[] getKey() {
		return key;
	}
	
	*//**
	 * @param {@link #key} setter
	 *//*
	public void setKey(byte[] key){
		this.key = key;
	}
	*/
}
