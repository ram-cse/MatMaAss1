package ass1.packet.encryption;

public class HashGenerationException extends Exception {
	
	public HashGenerationException(String string, Exception ex) {
		super(string, ex);
		
	}
	
	public HashGenerationException(String msg){
		super(msg);
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
