package bg.index.pdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

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
	    public static void readPDF(File f)
	    {

	        PdfReader reader;

	        try {

	            reader = new PdfReader(f.getAbsolutePath());

	            // pageNumber = 1
	            String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);

	            System.out.println(textFromPage);

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
