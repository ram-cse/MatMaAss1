package ass1.packet.client;

public class Config {
	
	private static AlgType AlgName = AlgType.RSA;
	
	public enum AlgType{
		RSA,
		DES,
		ABC
	}
	
	public static AlgType getAlgName() {
		return AlgName;
	}
	
	public static void setAlgName(AlgType algName) {
		AlgName = algName;
	}

}
