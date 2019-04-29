package bg.index.pdf;

import java.io.File;
import java.util.List;

import bg.tools.FileUtils;
import bg.tools.Logger;
import bg.tools.concurrent.MultiTaskMgr;
import bg.tools.concurrent.Task;

public class PDFImportTask implements Task {

	private File _file = null;
	public PDFImportTask(File f)
	{
		_file = f;
	}
	@Override
	public void run() {
		PDFUtil.readPDF(_file);

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	
	public  static void main(String args[])
	{
		List<File> files = FileUtils.getFilesByPath("C:\\Users\\jinjun.su\\OneDrive - HCL Technologies Ltd","pdf");
		//List<File> files = FileUtils.getFilesByPath("C:\\books","pdf");
		Logger.logInfo("file size:"+files.size());
		int count =1;
		for(File f:files)
		{
		//	Logger.logInfo("[name]->"+f.getAbsolutePath().toString()+" count:"+count++);
			MultiTaskMgr.getInstance(5).submitTask(new PDFImportTask(f) );
		}
		Logger.logInfo("total "+files.size()+" documents handled");
	
	}
}
