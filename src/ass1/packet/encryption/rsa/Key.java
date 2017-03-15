package ass1.packet.encryption.rsa;

import java.math.BigInteger;

public class Key {
	
	public Key(BigInteger n, BigInteger key) {
		this.n = n;
		this.key = key;
	}
	private BigInteger n;
	private BigInteger key;
	
	
	public BigInteger getN() {
		return n;
	}
	public void setN(BigInteger n) {
		this.n = n;
	}
	public BigInteger getKey() {
		return key;
	}
	public void setKey(BigInteger key) {
		this.key = key;
	}
	
	
}
