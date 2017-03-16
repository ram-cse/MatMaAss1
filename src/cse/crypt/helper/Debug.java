package cse.crypt.helper;

public class Debug {
	
	public static void d(Object msg){
		System.out.println("Debug: (" + msg+")");
	}

	public static void d(String tag, Object msg){
		System.out.println(tag +": " + msg +"");
	}
}
