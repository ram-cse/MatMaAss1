package cse.crypt.client;

import java.io.File;

public class Files {
	
	private File file;
	private final int READ_SIZE = 1024;
	
	public Files(String _file){
		try {
			File files = new File(_file);
			this.file = files;
		} catch (Exception e) {
			
		}
	}
	
	public String getName(){
		String fname = file.getName();
		return fname.replace(" ", "_");
	}
	
	public long getSize(){
		long f = (long)file.length();
		long size = (long) Math.ceil(f / READ_SIZE);
		return size;
	}

	public String getAbsolutePath(){
		return file.getAbsolutePath();
	}
}
