package ass1.packet.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.SecureRandom;

import ass1.packet.utils.Debug;

/**
 * @author vrams
 * 
 */
public class PairKeysGenerator {

	public static PairKeysGenerator getInstance() {
		PairKeysGenerator instance = new PairKeysGenerator();
		return instance;
	}

	private BigInteger n, d, e;
	private int bitlen = 1024;

	public PairKeysGenerator() {
		generateKey();
	}

	private void seek() {
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, r);
		BigInteger q = new BigInteger(bitlen / 2, 100, r);
		Debug.d("p", p);
		Debug.d("q", q);

		n = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		e = new BigInteger("3");
		while (m.gcd(e).intValue() > 1) {
			e = e.add(new BigInteger("1"));
		}
		Debug.d("E", e);
		d = e.modInverse(m);
		Debug.d("D ", d);
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getPublicKey() {
		return d;
	}

	public BigInteger getPrivateKey() {
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

	public static BigInteger readKeyFromFile(String path) {
		File file = new File(path);
		if (!file.exists() || !file.canRead()) {
			return null;
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer buffer = new StringBuffer();
			String curLine = null;
			while ((curLine = reader.readLine()) != null) {
				buffer.append(curLine);
			}
			reader.close();
			BigInteger result = new BigInteger(buffer.toString().getBytes());
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

		}
	}

}
