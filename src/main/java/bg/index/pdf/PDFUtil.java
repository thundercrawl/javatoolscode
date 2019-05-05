package bg.index.pdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import bg.tools.Logger;
import scn.index.ES.ESImporter;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFUtil {

	private List<String> filelist = null;
		public PDFUtil(ArrayList<String> filelist)
		{
			if(filelist!=null&&!filelist.isEmpty())
				this.filelist = filelist;
			else
				throw new RuntimeException("filelist cannot be empty and null");
				
		}

	    private static final String FILE_NAME = "/tmp/itext.pdf";

	    public static void main(String[] args) {
	        writeUsingIText();
	    }
	    public static void readPDFImportES(File f,ESImporter importer)
	    {

	        PdfReader reader;
	        try {

	            reader = new PdfReader(f.getAbsolutePath());
	            // pageNumber = 1
	            int pageSize = reader.getNumberOfPages();
	            //Logger.logInfo("page size:"+pageSize);
	    		int size = 0;
	            for(int idx=1;idx<=pageSize;idx++)
	            {
		            String textFromPage = PdfTextExtractor.getTextFromPage(reader, idx);
		            if(textFromPage==null)
		            	continue;
		            size+=textFromPage.length();
		            importer.putDoc(textFromPage, "bookcn", "bookContents", f.getName());
		        //  Logger.logInfo("Read string size:"+textFromPage.length()+" page number:"+idx);
		          //Logger.logInfo(textFromPage);
		         
	            }
	            float resultMbyte = size/1024/1024;
	            Logger.logInfo("document size:"+resultMbyte+" mbytes"+" page size:"+pageSize+" original size:"+size);
	            reader.close();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    }
	    public static void writeUsingIText() {

	        Document document = new Document();

	        try {

	            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

	            //open
	            document.open();

	            Paragraph p = new Paragraph();
	            p.add("This is my paragraph 1");
	            p.setAlignment(Element.ALIGN_CENTER);

	            document.add(p);

	            Paragraph p2 = new Paragraph();
	            p2.add("This is my paragraph 2"); //no alignment

	            document.add(p2);

	            Font f = new Font();
	            f.setStyle(Font.BOLD);
	            f.setSize(8);

	            document.add(new Paragraph("This is my paragraph 3", f));

	            //close
	            document.close();

	            System.out.println("Done");
	         
	        } catch (FileNotFoundException | DocumentException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    }
	}
