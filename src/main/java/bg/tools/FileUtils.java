package bg.tools;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import bg.index.pdf.PDFUtil;

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
				 if( listOfFiles[i].getName().endsWith(regex))
				  	files.add(listOfFiles[i]);
			    
			  } else if (listOfFiles[i].isDirectory()) {
			    ds.add(listOfFiles[i]);
			  }
			 
			}
			 ds.poll();
			  folder = ds.peek();
			  if(folder!=null)
				  listOfFiles = folder.listFiles();
			  else break;
			
		}
		return files;
		
	}
	
	
	public static void main(String[] args)
	{
		List<File> files = FileUtils.getFilesByPath("C:\\Users\\jinjun.su\\OneDrive - HCL Technologies Ltd","pdf");
		//List<File> files = FileUtils.getFilesByPath("C:\\books","pdf");
		Logger.logInfo("file size:"+files.size());
		int count =1;
		for(File f:files)
		{
			Logger.logInfo("[name]->"+f.getAbsolutePath().toString()+" count:"+count++);
			PDFUtil.readPDFImportES(f,null);
		}
		Logger.logInfo("total "+files.size()+" documents handled");
	} 
}
