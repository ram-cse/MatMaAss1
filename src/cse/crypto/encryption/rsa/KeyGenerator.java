package cse.crypto.encryption.rsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import cse.crypto.helper.Utils;

/**
 * @author vrams
 * 
 */
public class KeyGenerator {

	public static KeyGenerator getInstance() {
		KeyGenerator instance = new KeyGenerator();
		return instance;
	}

	private BigInteger n, d, e;
	private int bitlen = 1024; // 128 bytes

	public KeyGenerator() {
		generateKey();
	}

	private void seek() {
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen/2, 100, r);
		BigInteger q = new BigInteger(bitlen/2, 100, r);
		/*
		 * Debug.d("p", p); Debug.d("q", q);
		 */

		n = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		e = new BigInteger("3");
		while (m.gcd(e).intValue() > 1) {
			e = e.add(new BigInteger("1"));
		}
		/*
		 * Debug.d("E", e);
		 */ d = e.modInverse(m);
		/*
		 * Debug.d("D ", d);
		 */ }

	public BigInteger getN() {
		return n;
	}

	public BigInteger getD() {
		return d;
	}

	public BigInteger getE() {
		return e;
	}

	public void setKey(BigInteger n, BigInteger e, BigInteger d) {
		this.n = n;
		this.d = d;
		this.e = e;
	}

	public void setBitlen(int bitlen) {
		this.bitlen = bitlen;
	}

	public int getBitlen() {
		return bitlen;
	}

	public void generateKey() {
		seek();
	}

	public void generateKey(int bitlen) {
		setBitlen(bitlen);
		seek();
	}

	public void savePublicKey(String path) throws IOException {
		if (n == null) {
			throw new RuntimeException("n == null :3");
		}
		if (e == null) {
			throw new RuntimeException("e == null");
		}
		String content = getN() + "/" + getE();
		Utils.writeToFile(path, content);
	}

	public void savePrivateKey(String path) throws IOException {
		if (n == null) {
			throw new RuntimeException("n == null :3");
		}
		if (d == null) {
			throw new RuntimeException("d == null");
		}
		String content = getN() + "/" + getD();
		Utils.writeToFile(path, content);
	}

	public static Key readKeyFromFile(String file) throws IOException {
		String txt = Utils.readPlainFile(file);
		String key[] = txt.split("/");
		if (key.length != 2) {
			throw new RuntimeException("File containing key is invalid format: " + file);
		}
		Key k = new Key(new BigInteger(key[0]), new BigInteger(key[1]));
		return k;
	}

}
