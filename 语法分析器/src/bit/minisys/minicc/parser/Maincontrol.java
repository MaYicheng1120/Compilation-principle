package bit.minisys.minicc.parser;

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

import bit.minisys.minicc.scanner.Word;

public class Maincontrol {
	    //单词表
		ArrayList<Word> wordList = new ArrayList();
		//LL1分析栈
		Stack<AnalyseNode> analyseStack = new Stack();
		//错误信息表
		ArrayList<Errorinformaintion> errorList = new ArrayList();
		StringBuffer buf;
		int sumerror = 0;
		int sumtemp = 0;
		int fourElemCount = 0;
		int errorflage=0;
		Word firstWord;
	   	Errorinformaintion error;
	   	state node=new state();
	   	AnalyseNode root;//根节点
	   	//从词法分析生成的xml文件中读出单词
	   	public Maincontrol(String ifile) throws ParserConfigurationException, SAXException, IOException
	   	{
	   	// 得到DOM解析器的工厂实例
	   		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
	   	 // 从DOM工厂中获得DOM解析器 
	   		DocumentBuilder db=dbf.newDocumentBuilder();
	   	// 把要解析的xml文档读入DOM解析器
	   		org.w3c.dom.Document doc=db.parse(ifile);
	   	// 获取标签名为"token"的元素
	   		NodeList wordList=doc.getElementsByTagName("token");
	   	 // 下面依次读取token元素的每个子元素，每个子元素的标签名字为node 
	   		
	   		//读取完每个word后读取每个word的其他属性number value type line flag
	   		for (int i=0;i<wordList.getLength();i++)
	   		{
	   			Node token=wordList.item(i);
	   			Word word=new Word();
	   			Node node=token.getFirstChild();
	   			for (;node!=null;node=node.getNextSibling())
	   			{
	   				if (node.getNodeType()==1)
	   				{
	   					word.id=Integer.parseInt(node.getFirstChild().getNodeValue());
	   					break;
	   				}
	   			}
	   			for (node = node.getNextSibling(); node != null; node = node.getNextSibling()) 
	   			{
	   				if (node.getNodeType() == 1) 
	   				{
	   					word.value = node.getFirstChild().getNodeValue();
	   					break;
	   				}
	   			}
	   			for (node = node.getNextSibling(); node != null; node = node.getNextSibling())
	   			{
	   				if (node.getNodeType() == 1) 
	   				{
	   					word.type = node.getFirstChild().getNodeValue();
	   					break;
	   				}
	   			}
	   			for (node = node.getNextSibling(); node != null; node = node.getNextSibling()) 
	   			{
	   				if (node.getNodeType() == 1) 
	   				{
	   					word.line = Integer.parseInt(node.getFirstChild().getNodeValue());
	   					break;
	   				}
	   			}
	   			for (node = node.getNextSibling(); node != null; node = node.getNextSibling()) 
	   			{
	   				if (node.getNodeType() == 1) {
	   					word.flag = Boolean.getBoolean(node.getFirstChild().getNodeValue());
	   					break;
	   				}
	   			}
	   			this.wordList.add(word);
	   		}
	   		
	   	}
	   	//LL1总控程序
	   	public void LL1analyse()
	   	{
	   		
	   		
	   		int sumanalyse=0;
	   		this.buf=new StringBuffer(); //LL1分析表
	   		this.error = null;
	   		//this.analyseStack.add(0, this.PROGRAM);
	   		AnalyseNode eof= new AnalyseNode("#", "#", null, null);
	   		//this.analyseStack.add(1, eof);
	   		this.analyseStack.push(eof);
	   		this.analyseStack.push((AnalyseNode)node.PROGRAM);
	   		while((!this.analyseStack.empty())&&(!this.wordList.isEmpty()))
	   		{
	   			if(this.errorflage==1)
	   			{
	   				break;
	   			}
	   			//System.out.println(analyseStack.peek().value);
	   			
	   			sumanalyse++;
	   			this.buf.append("步骤"+sumanalyse+"\t");
	   			//System.out.println(sumanalyse);
	   			this.root=((AnalyseNode)this.analyseStack.peek());
	   			this.firstWord=((Word)this.wordList.get(0));
	   			//System.out.println(this.root.wordType);
	   			//System.out.println(wordList);
	   			
	   			//分析成功
	   			if ((this.firstWord.value.equals("#"))&&(this.root.name.equals("#"))) 
	   			{
	   				this.buf.append("\n");
	   				this.analyseStack.pop();
	   				this.wordList.remove(0);
	   				break;
	   			}
	   			//当分析栈中为结束符号但余留输入串栈顶不为结束符号时分析出错
	   			if(!this.firstWord.value.equals("#")&&(this.root.name.equals("#")))
	   			{
	   				//System.out.println("vegeggt"+this.firstWord.value);
	   				//修改错误标记后结束分析并给出错误提示
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror, "error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				break;
	   			}
	   			//分析栈中为终结符
	   			if (this.root.type.equals("terminal"))
				{
	   				//System.out.println("342423423423");
	   				 String word=this.root.name;
	   				  if((this.firstWord.type.equals("GiG"))||(word.equals(this.firstWord.value))||(this.firstWord.type.equals("GjG"))|| 
	   			   			(word.equals(this.firstWord.value))||(this.firstWord.type.equals("const_i"))||
	   			   			((word.equals("id"))&&(this.firstWord.type.equals("GiG"))))
	   			   	{
	   				    	//分析栈栈顶与余留输入串栈顶元素相等同时出栈，此步分析完成\
	   					  	this.analyseStack.peek().name=this.firstWord.value;
	   					  	this.analyseStack.peek().value=this.firstWord.value;
	   				    	this.analyseStack.pop();
	   				    	this.wordList.remove(0);
	   				    	
	   			   			
	   			   		
	   			   	} 
	   				    //单词未定义
	   				else 
	   				{
	   					this.sumerror += 1;
	   			   		this.analyseStack.pop();
	   			   		this.wordList.remove(0);
	   			   		this.error = new Errorinformaintion(this.sumerror, "Undefined", this.firstWord.line, this.firstWord.value);
	   			   		this.errorList.add(this.error);
	   			   		
	   				}
	   				   
				}
	   			//分析栈中为非终结符
				else if (this.root.type.equals("nonterminal"))
				{
					
					analyse(this.root.name);
					
				}
	   			this.buf.append("分析栈:");
	   			for (int i = 0; i < this.analyseStack.size(); i++) {
	   			this.buf.append(((AnalyseNode)this.analyseStack.get(i)).name+" ");
	   			}
	   			this.buf.append("\t").append("余留输入串：");
	   			for (int j = 0; j < this.wordList.size(); j++) {
	   				this.buf.append(((Word)this.wordList.get(j)).value+" ");
	   			}
	   			
	   			this.buf.append("\r\n");
	   			this.buf.append("\r\n");
	   		}
	   		
	   		
	   		
	   	}
		public String writeLL1()
				   throws IOException
		   	{
		   		String path = ".\\input\\LL1.txt";
		   		FileOutputStream fos = new FileOutputStream(path);
		 
		   		BufferedOutputStream bos = new BufferedOutputStream(fos);
		   		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		   		PrintWriter pw1 = new PrintWriter(osw1);
		   		pw1.println(this.buf.toString());
		   		this.buf.delete(0, this.buf.length());
		   		
		   		if (this.errorflage==1)
		   		{
		   			pw1.println("错误信息如下：");
		   			pw1.println("错误序号\t错误信息\t错误所在行\t错误单词");
		   			for (int i = 0; i < this.errorList.size(); i++) {
		   				Errorinformaintion error = (Errorinformaintion)this.errorList.get(i);
		   				pw1.println(error.sumerror + "\t" + error.errortype + "\t\t" + error.line + "\t" + error.errorword);
		   			}
		   		} else {
		   			pw1.println("acc");
		   		}
		   		pw1.close();
		   		return path;
		   	}
	    public void writexml(String oFile)throws IOException
	    {
	    	Element root = new Element("ParserTree").setAttribute("name", "test.tree.xml");
	    	org.jdom2.Document Doc = new org.jdom2.Document(root);
	    	Element start = new Element("PROGRAM");
	   		root.addContent(start);
	   		
	   		AnalyseNode node1 = node.PROGRAM.firstChild;
	   		deal(start, node1);
	 
	   		Format format = Format.getPrettyFormat();
	   		XMLOutputter XMLOut = new XMLOutputter(format);
	   		XMLOut.output(Doc, new FileOutputStream(oFile));
	 
	   		return ;
	    }
	    private void deal(Element e, AnalyseNode node) {
	   		if ((node == null) || (node.hasDealt))
	   			return;
	   		Element element = null;
	   		if (node.wordType == null)
   			{
   				if (node.value == null)
   				{
   					element = new Element(node.name);
   				}
   				else
   					element = new Element(node.value);
   			}
   			else
   			{
   				element= new Element(node.wordType).setText(node.value);
   			}
     
   			e.addContent(element);
   			//node.hasDealt = true;
   			AnalyseNode temp = node.nextSubling;
   			if (temp != null) {
   				deal(e, temp);
   			}
   			deal(element, node.firstChild);
  }
	    private void analyse(String str)
	   	{
	   		if(str.equals("PROGRAM"))
	   		{
	   			this.analyseStack.pop();
	   			AnalyseNode a=new AnalyseNode(node.FUNCTIONS);
	   			this.root.firstChild = a;
				this.analyseStack.push(a);
	   		}
	   		else if(str.equals("FUNCTIONS"))
	   		{
	   			if(this.firstWord.value.equals("flaot")||this.firstWord.value.equals("int"))
	   			{
	   				this.analyseStack.pop();
	   				AnalyseNode a=new AnalyseNode(node.FUNCTION);
	   				this.root.firstChild = a;
	   				AnalyseNode b=new AnalyseNode(node.FUNLIST);
	   				a.nextSubling=b;
	   				this.analyseStack.push(b);
	   				this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   			
	   		}
	   		else if(str.equals("FUNLIST"))
	   		{
	   			if(this.firstWord.value.equals("flaot")||this.firstWord.value.equals("int"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.FUNCTIONS);
		   			this.root.firstChild = a;
					this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("#"))
	   				this.analyseStack.pop();
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
		   	}
	   		else if(str.equals("FUNCTION"))
	   		{
	   			if(this.firstWord.value.equals("flaot")||this.firstWord.value.equals("int"))
	   			{
	   				this.analyseStack.pop();
	   				AnalyseNode a=new AnalyseNode(node.TYPE);
	   				this.root.firstChild = a;
	   				AnalyseNode b=new AnalyseNode(node.TKN_ID);
	   				a.nextSubling=b;
	   				AnalyseNode c=new AnalyseNode(node.TKN_LP);
	   				b.nextSubling=c;
	   				AnalyseNode d=new AnalyseNode(node.ARGS);
	   				c.nextSubling=d;
	   				AnalyseNode e=new AnalyseNode(node.TKN_RP);
	   				d.nextSubling=e;
	   				AnalyseNode f=new AnalyseNode(node.FUNC_BODY);
	   				e.nextSubling=f;
	   				this.analyseStack.push(f);
	   				this.analyseStack.push(e);
	   				this.analyseStack.push(d);
	   				this.analyseStack.push(c);
	   				this.analyseStack.push(b);
	   				this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   			
	   		}
	   		else if(str.equals("ARGS"))
	   		{
	   			if(this.firstWord.value.equals(")"))
	   			{
	   				this.analyseStack.pop();
	   			}
	   			else if(this.firstWord.value.equals("int")||this.firstWord.value.equals("float"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.FARGS);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.ALIST);
		   			a.nextSubling=b;
					this.analyseStack.push(b);
					this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   		else if(str.equals("FARGS"))
	   		{
	   			if(this.firstWord.value.equals("flaot")||this.firstWord.value.equals("int"))
	   			{
	   				this.analyseStack.pop();
	   				AnalyseNode a=new AnalyseNode(node.TYPE);
	   				this.root.firstChild = a;
	   				AnalyseNode b=new AnalyseNode(node.TKN_ID);
	   				a.nextSubling=b;
	   				this.analyseStack.push(b);
	   				this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   			
	   		}
	   		else if(str.equals("ALIST"))
	   		{
	   			if(this.firstWord.value.equals(")"))
	   			{
	   				this.analyseStack.pop();
	   			}
	   			else if(this.firstWord.value.equals(","))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_COMMA);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.FARGS);
		   			a.nextSubling=b;
		   			AnalyseNode c=new AnalyseNode(node.ALIST);
		   			b.nextSubling=c;
		   			this.analyseStack.push(c);
		   			this.analyseStack.push(b);
		   			this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   		else if(str.equals("FUNC_BODY"))
	   		{
	   			this.analyseStack.pop();
	   			AnalyseNode a=new AnalyseNode(node.TKN_LB);
	   			this.root.firstChild = a;
	   			AnalyseNode b=new AnalyseNode(node.STMTS);
	   			a.nextSubling=b;
	   			AnalyseNode c=new AnalyseNode(node.TKN_RB);
	   			b.nextSubling=c;
	   			this.analyseStack.push(c);
	   			this.analyseStack.push(b);
	   			this.analyseStack.push(a);
	   		}
	   		else if(str.equals("STMTS"))
	   		{
	   			
	   			if(this.firstWord.type.equals("GiG")||this.firstWord.value.equals("(")
	   					||this.firstWord.value.equals(")")||this.firstWord.value.equals("return")
	   					||this.firstWord.value.equals(";")||this.firstWord.value.equals("int")
	   					||this.firstWord.value.equals("float")||this.firstWord.value.equals("for")
	   					||this.firstWord.value.equals("if"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.STMT);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.STMTS);
		   			a.nextSubling=b;
					this.analyseStack.push(b);
					this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("}"))
	   			{
	   				this.analyseStack.pop();
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   			
	   		}
	   		else if(str.equals("STMT"))
	   		{
	   			if(this.firstWord.type.equals("GiG")||this.firstWord.value.equals("(")
	   					||this.firstWord.value.equals(")")||
	   					this.firstWord.value.equals(";")||this.firstWord.value.equals("int")
	   					||this.firstWord.value.equals("float"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.EXPR_STMT);
		   			this.root.firstChild = a;
					this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("return"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.RET_STMT);
		   			this.root.firstChild = a;
					this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("for"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.FOR_STMT);
		   			this.root.firstChild = a;
					this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("if"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.IF_STMT);
		   			this.root.firstChild = a;
					this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
					
	   		}
	   		else if(str.equals("EXPR_STMT"))
	   		{
	   			if(this.firstWord.type.equals("GiG")||this.firstWord.value.equals("(")
	   					||this.firstWord.value.equals(")")||this.firstWord.value.equals(";")||this.firstWord.value.equals("int")
	   					||this.firstWord.value.equals("float"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.EXPR);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.TKN_SEMICOLON);
		   			a.nextSubling=b;
					this.analyseStack.push(b);
					this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   		else if(str.equals("EXPR"))
	   		{
	   			if(this.firstWord.type.equals("GiG")||this.firstWord.value.equals("(")||this.firstWord.type.equals("GjG"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.FACTOR);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.FLIST);
		   			a.nextSubling=b;
					this.analyseStack.push(b);
					this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals(")")||this.firstWord.value.equals(";")
	   					||this.firstWord.value.equals("int")
	   					||this.firstWord.value.equals("float"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.EARGS);
		   			this.root.firstChild = a;
					this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   		else if(str.equals("FACTOR"))
	   		{
	   			
	   			if(this.firstWord.type.equals("GiG")||this.firstWord.type.equals("GjG"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_ID);
		   			this.root.firstChild = a;
					this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("("))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_LP);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.EXPR);
		   			a.nextSubling=b;
		   			AnalyseNode c=new AnalyseNode(node.TKN_RP);
		   			b.nextSubling=c;
		   			this.analyseStack.push(c);
		   			this.analyseStack.push(b);
		   			this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   			
	   		}
	   		else if(str.equals("FLIST"))
	   		{
	   			if(this.firstWord.value.equals("+"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_PLUS);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.FACTOR);
		   			a.nextSubling=b;
		   			AnalyseNode c=new AnalyseNode(node.FLIST);
		   			b.nextSubling=c;
		   			this.analyseStack.push(c);
		   			this.analyseStack.push(b);
		   			this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("<")||this.firstWord.value.equals(">"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_LESS);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.FACTOR);
		   			a.nextSubling=b;
		   			AnalyseNode c=new AnalyseNode(node.FLIST);
		   			b.nextSubling=c;
		   			this.analyseStack.push(c);
		   			this.analyseStack.push(b);
		   			this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals("="))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_ASN);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.FACTOR);
		   			a.nextSubling=b;
		   			AnalyseNode c=new AnalyseNode(node.FLIST);
		   			b.nextSubling=c;
		   			this.analyseStack.push(c);
		   			this.analyseStack.push(b);
		   			this.analyseStack.push(a);
	   			}
	   			else if(this.firstWord.value.equals(")")||
	   					this.firstWord.value.equals(";"))
	   				this.analyseStack.pop();
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   		else if(str.equals("EARGS"))
	   		{
	   			if(this.firstWord.value.equals(")")||
	   					this.firstWord.value.equals(";"))
	   				this.analyseStack.pop();
	   			else if(this.firstWord.value.equals("int")||
	   					this.firstWord.value.equals("float"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.FARGS);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.EALIST);
		   			a.nextSubling=b;
					this.analyseStack.push(b);
					this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   		else if(str.equals("EALIST"))
	   		{
	   			
	   			if(this.firstWord.value.equals(")")||
	   					this.firstWord.value.equals(";"))
	   				this.analyseStack.pop();
	   			else if(this.firstWord.value.equals(","))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_COMMA);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.TKN_ID);
		   			a.nextSubling=b;
		   			AnalyseNode c=new AnalyseNode(node.EALIST);
		   			b.nextSubling=c;
		   			this.analyseStack.push(c);
		   			this.analyseStack.push(b);
		   			this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   		else if(str.equals("RET_STMT"))
	   		{
	   			if(this.firstWord.value.equals("return"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_KW_RET);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.EXPR_STMT);
		   			a.nextSubling=b;
					this.analyseStack.push(b);
					this.analyseStack.push(a);
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   			
	   		}
	   		else if(str.equals("FOR_STMT"))
	   		{
	   			if(this.firstWord.value.equals("for"))
	   			{
	   				this.analyseStack.pop();
		   			AnalyseNode a=new AnalyseNode(node.TKN_FOR);
		   			this.root.firstChild = a;
		   			AnalyseNode b=new AnalyseNode(node.TKN_LP);
		   			a.nextSubling=b;
		   			AnalyseNode c=new AnalyseNode(node.EXPR_STMT);
		   			b.nextSubling=c;
		   			AnalyseNode d=new AnalyseNode(node.EXPR_STMT);
		   			c.nextSubling=d;
		   			AnalyseNode e=new AnalyseNode(node.EXPR);
		   			d.nextSubling=e;
		   			AnalyseNode f=new AnalyseNode(node.TKN_RP);
		   			e.nextSubling=f;
		   			AnalyseNode g=new AnalyseNode(node.TKN_LB);
		   			f.nextSubling=g;
		   			AnalyseNode h=new AnalyseNode(node.STMTS);
		   			g.nextSubling=h;
		   			AnalyseNode i=new AnalyseNode(node.TKN_RB);
		   			h.nextSubling=i;
		   			this.analyseStack.push(i);
		   			this.analyseStack.push(h);
		   			this.analyseStack.push(g);
		   			this.analyseStack.push(f);
		   			this.analyseStack.push(e);
		   			this.analyseStack.push(d);
		   			this.analyseStack.push(c);
		   			this.analyseStack.push(b);
		   			this.analyseStack.push(a);
					
	   			}
	   			else
	   			{
	   				this.sumerror += 1;
			   		this.analyseStack.pop();
			   		this.wordList.remove(0);
			   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
			   		this.errorList.add(this.error);
	   				this.errorflage=1;
	   				return;
	   			}
	   		}
	   			else if(str.equals("IF_STMT"))
		   		{
		   			if(this.firstWord.value.equals("if"))
		   			{
		   				this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.TKN_IF);
			   			this.root.firstChild = a;
			   			AnalyseNode b=new AnalyseNode(node.TKN_LP);
			   			a.nextSubling=b;
			   			AnalyseNode c=new AnalyseNode(node.EXPR);
			   			b.nextSubling=c;
			   			AnalyseNode d=new AnalyseNode(node.TKN_RP);
			   			c.nextSubling=d;
			   			AnalyseNode e=new AnalyseNode(node.TKN_LB);
			   			d.nextSubling=e;
			   			AnalyseNode f=new AnalyseNode(node.STMTS);
			   			e.nextSubling=f;
			   			AnalyseNode g=new AnalyseNode(node.TKN_RB);
			   			f.nextSubling=g;
			   			AnalyseNode h=new AnalyseNode(node.ELSEIF);
			   			g.nextSubling=h;
			   			this.analyseStack.push(h);
			   			this.analyseStack.push(g);
			   			this.analyseStack.push(f);
			   			this.analyseStack.push(e);
			   			this.analyseStack.push(d);
			   			this.analyseStack.push(c);
			   			this.analyseStack.push(b);
			   			this.analyseStack.push(a);
						
		   			}
		   			else
		   			{
		   				this.sumerror += 1;
				   		this.analyseStack.pop();
				   		this.wordList.remove(0);
				   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
				   		this.errorList.add(this.error);
		   				this.errorflage=1;
		   				return;
		   			}
		   			
		   		}
	   			else if(str.equals("ELSEIF"))
	   			{
	   				if(this.firstWord.value.equals("}"))
	   					this.analyseStack.pop();
	   				else if(this.firstWord.value.equals("else"))
	   				{
	   					this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.TKN_ELSE);
			   			this.root.firstChild = a;
			   			AnalyseNode b=new AnalyseNode(node.ILIST);
			   			a.nextSubling=b;
						this.analyseStack.push(b);
						this.analyseStack.push(a);
	   				}
	   				else if(this.firstWord.value.equals("return"))
	   				{
	   					this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.RET_STMT);
			   			this.root.firstChild = a;
			   			this.analyseStack.push(a);
	   				}
	   				else if(this.firstWord.type.equals("GiG")||this.firstWord.value.equals("int")||
	   						this.firstWord.value.equals("float")||this.firstWord.value.equals("for")||
	   						this.firstWord.value.equals("if"))
	   				{
	   					this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.STMTS);
			   			this.root.firstChild = a;
			   			this.analyseStack.push(a);
	   				}
	   				else
		   			{
		   				this.sumerror += 1;
				   		this.analyseStack.pop();
				   		this.wordList.remove(0);
				   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
				   		this.errorList.add(this.error);
		   				this.errorflage=1;
		   				return;
		   			}
	   			}
	   			else if(str.equals("ILIST"))
	   			{
	   				if(this.firstWord.value.equals("}"))
	   				{
	   					this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.TKN_LB);
			   			this.root.firstChild = a;
			   			AnalyseNode b=new AnalyseNode(node.STMTS);
			   			a.nextSubling=b;
			   			AnalyseNode c=new AnalyseNode(node.TKN_RB);
			   			b.nextSubling=c;
			   			this.analyseStack.push(c);
			   			this.analyseStack.push(b);
			   			this.analyseStack.push(a);
	   				}
	   				else if(this.firstWord.value.equals("if"))
	   				{
	   					this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.IF_STMT);
			   			this.root.firstChild = a;
			   			this.analyseStack.push(a);
	   				}
	   				else
		   			{
		   				this.sumerror += 1;
				   		this.analyseStack.pop();
				   		this.wordList.remove(0);
				   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
				   		this.errorList.add(this.error);
		   				this.errorflage=1;
		   				return;
		   			}
	   			}
	   			else if(str.equals("TYPE"))
	   			{
	   				if(this.firstWord.value.equals("int"))
	   				{
	   					this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.TKN_INT);
			   			this.root.firstChild = a;
			   			this.analyseStack.push(a);
	   				}
	   				else if(this.firstWord.value.equals("float"))
	   				{
	   					this.analyseStack.pop();
			   			AnalyseNode a=new AnalyseNode(node.TKN_FLOAT);
			   			this.root.firstChild = a;
			   			this.analyseStack.push(a);
	   				}
	   				else
		   			{
		   				this.sumerror += 1;
				   		this.analyseStack.pop();
				   		this.wordList.remove(0);
				   		this.error = new Errorinformaintion(this.sumerror,"Syntax error", this.firstWord.line, this.firstWord.value);
				   		this.errorList.add(this.error);
		   				this.errorflage=1;
		   				return;
		   			}
	   			}
	   			
	   		
	   
	   	}
	    
}
