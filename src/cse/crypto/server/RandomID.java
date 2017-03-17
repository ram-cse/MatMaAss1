package cse.crypto.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomID {
	
	private static List<Integer> ids = new ArrayList<Integer>();
	private static final int RANGE = 10000;
	
	private static int index = 0;
	
	static {
		for (int i = 0; i < RANGE; i++) {
			ids.add(i);
		}
		Collections.shuffle(ids);
	}
	
	private RandomID() {
	}
	
	public static int getIdentifier() {
		if (index > ids.size() - 1) index = 0;
		return ids.get(index++);
	}
	
}
