package bit.minisys.minicc.parser;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import bit.minisys.minicc.scanner.Error;
import bit.minisys.minicc.scanner.Word;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MiniCCParser implements IMiniCCParser {
	public void run(String iFile, String oFile) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		try {
		Maincontrol myparser=new Maincontrol(iFile);
		myparser.LL1analyse();
		if(myparser.errorflage==1)
		{
			System.out.println("ERROR!");
			System.out.println("ERROR Informaintion:");
			for (int i=0;i<myparser.errorList.size();i++)
			{
   				Errorinformaintion error = (Errorinformaintion)myparser.errorList.get(i);
   				System.out.println(error.sumerror+" " +error.errortype+" " + error.line + " "+error.errorword);
   			}
			return;
		}
		
		myparser.writeLL1();
		myparser.writexml(oFile);
		System.out.println("3.语法分析完成！");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
