package bg.index.pdf;

import java.io.File;
import java.util.List;

import bg.tools.FileUtils;
import bg.tools.Logger;
import bg.tools.concurrent.MultiTaskMgr;
import bg.tools.concurrent.Task;
import scn.index.ES.ESImporter;

public class PDFImportTask implements Task {

	private File _file = null;
	private ESImporter _importer;
	public PDFImportTask(File f,ESImporter importer)
	{
		_file = f;
		_importer = importer;
		
	}
	@Override
	public void run() {
		try{
			PDFUtil.readPDFImportES(_file,_importer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	
	public  static void main(String args[])
	{
		//List<File> files = FileUtils.getFilesByPath("G:\\OneDrive - HCL Technologies Ltd","pdf");
		List<File> files = FileUtils.getFilesByPath("C:\\Users\\jinjun.su\\OneDrive - HCL Technologies Ltd","pdf");
		Logger.logInfo("file size:"+files.size());
		int count =1;
		for(File f:files)
		{
		//	Logger.logInfo("[name]->"+f.getAbsolutePath().toString()+" count:"+count++);
			MultiTaskMgr.getInstance(8).submitTask(new PDFImportTask(f,new ESImporter("47.105.127.77", 9200) ) );
		}
		Logger.logInfo("total "+files.size()+" documents handled");
		long start = System.currentTimeMillis();
		MultiTaskMgr.getInstance(10).waitForExit();
		//97 seconds 6700k,149 documents
		Logger.logInfo("Total processing time:"+(System.currentTimeMillis()-start)/1000+" seconds");
	
	}
}
