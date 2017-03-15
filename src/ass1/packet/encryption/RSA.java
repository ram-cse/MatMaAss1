package ass1.packet.encryption;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author vrams
 *
 */
public class RSA {
	private BigInteger n;

	/** Create an instance that can encrypt using someone else public key. */
	public RSA(BigInteger n) {
		this.n = n;
	}

	/** Encrypt the given plaintext message. */
	public synchronized String encrypt(byte[] message, BigInteger publicKey) {
		return (new BigInteger(message)).modPow(publicKey, n).toString();
	}

	/** Encrypt the given plaintext message. */
	public synchronized BigInteger encrypt(BigInteger message, BigInteger publicKey) {
		return message.modPow(publicKey, n);
	}

	/** Decrypt the given ciphertext message. */
	public synchronized byte[] decrypt(byte[] message, BigInteger privateKey) {
		return (new BigInteger(message)).modPow(privateKey, n).toByteArray();
	}

	/** Decrypt the given ciphertext message. */
	public synchronized BigInteger decrypt(BigInteger message, BigInteger privateKey) {
		return message.modPow(privateKey, n);
	}

	/** Return the modulus. */
	public synchronized BigInteger getN() {
		return n;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	/** Trivial test program. */
	public static void main(String[] args) {
		PairKeysGenerator key = new PairKeysGenerator();
		key.generateKey();
		RSA rsa = new RSA(key.getN());

		String text1 = "Yellow and Black Border Collies";
		System.out.println("Plaintext: " + text1);
		BigInteger plaintext = new BigInteger(text1.getBytes());

		BigInteger ciphertext = rsa.encrypt(plaintext, key.getPrivateKey());
		System.out.println("Ciphertext: " + ciphertext);
		plaintext = rsa.decrypt(ciphertext, key.getPublicKey());

		String text2 = new String(plaintext.toByteArray());
		System.out.println("Plaintext: " + text2);
	}
}
