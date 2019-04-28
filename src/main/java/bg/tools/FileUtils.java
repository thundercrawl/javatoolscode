package bg.tools;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FileUtils {

	/*
	 *  path = c:/
	 *  regex=pdf|txt liked
	 */
	public static List<File> getFilesByPath(String path,String regex)
	{
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		List<File> files = new ArrayList<File>();
		ArrayDeque<File> ds = new ArrayDeque<File>();
		ds.add(folder);
		while(!ds.isEmpty())
		{
			for (int i = 0; i < listOfFiles.length; i++) {
			  if (listOfFiles[i].isFile()) {
				  	files.add(listOfFiles[i]);
			    
			  } else if (listOfFiles[i].isDirectory()) {
			    ds.add(listOfFiles[i]);
			  }
			  ds.poll();
			  folder = ds.peek();
			  if(folder!=null)
				  listOfFiles = folder.listFiles();
			  else break;
			}
			
		}
		return files;
		
	}
	
	
	public static void main(String[] args)
	{
		FileUtils.getFilesByPath("c:\\","pdf");
	}
}
