 package bit.minisys.minicc.scanner;
/**
 * 词法分析
 * @author 1320151094 GJLDR
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import bit.minisys.minicc.scanner.judge;
import bit.minisys.minicc.util.MiniCCUtil;


public class MiniCCScanner extends judge implements IMiniCCScanner{
	private String keyWords[] ={"auto","break","case",
			"const","char","continue","default","do",
			"double","else","extern","enum","float","for",
			"if","inline","int","long","printf","register","restrict","return","short","signed","sizeof","static",
			"switch","struct","typedef","union","unsigned","void","volatile","while","_Alignas",
			"_Alignof","_Atomic","_Bool","_Complex","_Genertic","_Imaginary",
			"_Noreturn","_Static_assert","_Thread_local","void","while","for","scanf"}; // 运算符数组
	private char separators[] = {',',';','{','}','(',')','[',']','_',
			':','、','.','"' }; // 分隔符数组
	private String eof="#";//文件结束标志
	private String buffer = new String(); // 缓冲区
	private  String Fileinformaintion="";//输出文件缓冲区
	private int i=0;//缓冲区指针
	private int sum=0;//单词的总个数
	private int line=0;//当前行数
	private char ch; //存放最新读进的源程序字符
	private String Word; //存放构成单词符号的字符串
	private String type;
	private String value;
	/*词法分析*/
	//组织输入
	public String readfile(String filepath) throws IOException
	{
		
		
	    BufferedReader reader=new BufferedReader(new FileReader(filepath));
	    
	    Fileinformaintion+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<project name=\"test.c\">\n  <tokens>\n";
	    while((buffer=reader.readLine())!=null)
	    {
	    	line++;
	    	//System.out.println("line:"+line+buffer);
	    	//Fileinformaintion+="    <token>\n";
	    	scan();
	    	//System.out.println(Fileinformaintion);
	    }
	    line++;
	    buffer="#";
	    scan();//文件结尾标志
	    Fileinformaintion+="  </tokens>\n</project>\r\n";
	    reader.close();
	    return Fileinformaintion;
	}
	//扫描分析
	public void scan() throws IOException
	{
		i=0;
		//System.out.println(buffer);
		Word="";
		while(i<buffer.length())
		{
			ch=buffer.charAt(i);
			i++;
		    while(Character.isWhitespace(ch))//判断是否为空白字符。
			{
				if(i>=buffer.length())
					break;
				ch=buffer.charAt(i);//若是则从缓冲区中读取下一个字符直至非空白字符为止
				i++;
			}
			
			if(letter(ch))//字符为字母
			{
				while(letter(ch)||number(ch))//组成单词
				{
					Word=Word+ch;
					ch=buffer.charAt(i);
					i++;
					
				}
				i--;//退回一个已经读进的字符
				ch=' ';
				if(keyWord(Word))//如果单词为关键字
				{
					sum++;
					Fileinformaintion+="    <token>\n";
					writefile(Word,"keyword");
					
				}
				else//单词为标识符
				{
					sum++;
					Fileinformaintion+="    <token>\n";
					Fileinformaintion+="      <number>"+sum+"</number>\n";
					Fileinformaintion+="      <value>"+Word+"</value>\n";
					Fileinformaintion+="      <type>"+"GiG"+"</type>\n";
					Fileinformaintion+="      <line>"+line+"</line>\n";
			    	Fileinformaintion+="      <valid>true</valid>\n";
			    	Fileinformaintion+="    </token>\n";
				}
				Word="";//将单词清空回到原始状态
				continue;
			}
			else if(number(ch))//ch为数字
			{
				while(number(ch))
					{
						Word+=ch;
						ch=buffer.charAt(i);
						i++;
					}
				i--;
				ch=' ';
				sum++;
				//整数
				Fileinformaintion+="    <token>\n";
				Fileinformaintion+="      <number>"+sum+"</number>\n";
				Fileinformaintion+="      <value>"+Word+"</value>\n";
				Fileinformaintion+="      <type>"+"GjG"+"</type>\n";
				Fileinformaintion+="      <line>"+line+"</line>\n";
		    	Fileinformaintion+="      <valid>true</valid>\n";
		    	Fileinformaintion+="    </token>\n";
//				if(!letter(ch))//如果数字后面是字母或符号则为整数
//				{
//					i--;
//					ch=' ';
//					sum++;
//					writefile(sum,Word,"GjG",line,true);//整数
//				}
				Word="";
				continue;
			}
			else if(operator(ch))//运算符
			{
				
				switch(ch)
				{//运算符加减乘除等
					case'+':sum++;
					ch=buffer.charAt(i);
					i++;
					if(ch=='+')
					{
						Fileinformaintion+="    <token>\n";
						writefile("++","operator");
						break;
					}
					if(ch=='=')
					{
						Fileinformaintion+="    <token>\n";
						writefile("+=","operator");
						break;
					}
					Fileinformaintion+="    <token>\n";
					writefile("+","operator");
					i--;
					ch=' ';
					break;
					case'-':sum++;
					ch=buffer.charAt(i);
					i++;
					if(ch=='-')
					{
						Fileinformaintion+="    <token>\n";
						writefile("--","operator");
						break;
					}
					if(ch=='=')
					{
						Fileinformaintion+="    <token>\n";
						writefile("-=","operator");
						break;
					}
					Fileinformaintion+="    <token>\n";
					writefile("-","operator");
					i--;
					ch=' ';
					break;
					case'*':sum++;
					Fileinformaintion+="    <token>\n";
					Fileinformaintion+="      <number>"+sum+"</number>\n";
					Fileinformaintion+="      <value>"+"*"+"</value>\n";
					Fileinformaintion+="      <type>"+"operator"+"</type>\n";
					Fileinformaintion+="      <line>"+line+"</line>\n";
			    	Fileinformaintion+="      <valid>true</valid>\n";
			    	Fileinformaintion+="    </token>\n";break;
					case'/':sum++;
					Fileinformaintion+="    <token>\n";
					Fileinformaintion+="      <number>"+sum+"</number>\n";
					Fileinformaintion+="      <value>"+"/"+"</value>\n";
					Fileinformaintion+="      <type>"+"operator"+"</type>\n";
					Fileinformaintion+="      <line>"+line+"</line>\n";
			    	Fileinformaintion+="      <valid>true</valid>\n";
			    	Fileinformaintion+="    </token>\n";break;
					case'>':sum++;
					ch=buffer.charAt(i);
					i++;
					if(ch=='=')
					{
						Fileinformaintion+="    <token>\n";
						writefile(">=","operator");
						break;
					}
					Fileinformaintion+="    <token>\n";
					writefile(">","operator");
					i--;
					ch=' ';
					break;
					case'<':sum++;
					ch=buffer.charAt(i);
					i++;
					if(ch=='+')
					{
						Fileinformaintion+="    <token>\n";
						writefile("++","operator");
						break;
					}
					if(ch=='=')
					{
						Fileinformaintion+="    <token>\n";
						writefile("<=","operator");
						break;
					}
					Fileinformaintion+="    <token>\n";
					writefile("<","operator");
					i--;
					ch=' ';
					break;
//					case'!':sum++;
//					ch=buffer.charAt(i);
//					i++;
//					if(ch=='=')
//					{
//						writefile("!=","operator");
//						break;
//					}
//					writefile("!","operator");
//					i--;
//					ch=' ';
//					break;
					case'=':sum++;
					Fileinformaintion+="    <token>\n";
					writefile("=","operator");
					
					break;
					case'&':sum++;
					ch=buffer.charAt(i);
					i++;
					if(ch=='&')
					{
						Fileinformaintion+="    <token>\n";
						writefile("&&","operator");
						break;
					}
					Fileinformaintion+="    <token>\n";
					writefile("&","operator");
					i--;
					ch=' ';
					break;
					case'|':sum++;
					ch=buffer.charAt(i);
					i++;
					if(ch=='|')
					{
						Fileinformaintion+="    <token>\n";
						writefile("||","operator");
						break;
					}
					Fileinformaintion+="    <token>\n";
					writefile("|","operator");
					i--;
					ch=' ';
					break;
					case'~':sum++;
					Fileinformaintion+="    <token>\n";
					Fileinformaintion+="      <number>"+sum+"</number>\n";
					Fileinformaintion+="      <value>"+"~"+"</value>\n";
					Fileinformaintion+="      <type>"+"operator"+"</type>\n";
					Fileinformaintion+="      <line>"+line+"</line>\n";
			    	Fileinformaintion+="      <valid>true</valid>\n";
			    	Fileinformaintion+="    </token>\n";break;
					default:break;
				}
				continue;	
			}
			else if(separators(ch))//界限符
			{
				if(ch=='"')
				{
					Word+=ch;
					ch=buffer.charAt(i);
					i++;
					while(ch!='"')
					{
						Word+=ch;
						ch=buffer.charAt(i);
						i++;
					}
					Word+=ch;
					sum++;
					Fileinformaintion+="    <token>\n";
					Fileinformaintion+="      <number>"+sum+"</number>\n";
					Fileinformaintion+="      <value>"+Word+"</value>\n";
					Fileinformaintion+="      <type>"+"GkG"+"</type>\n";
					Fileinformaintion+="      <line>"+line+"</line>\n";
					Fileinformaintion+="      <valid>true</valid>\n";
					Fileinformaintion+="    </token>\n";
					continue;
				}
				else
				{
					sum++;
					Fileinformaintion+="    <token>\n";
					Fileinformaintion+="      <number>"+sum+"</number>\n";
					Fileinformaintion+="      <value>"+ch+"</value>\n";
					Fileinformaintion+="      <type>"+"separator"+"</type>\n";
					Fileinformaintion+="      <line>"+line+"</line>\n";
					Fileinformaintion+="      <valid>true</valid>\n";
					Fileinformaintion+="    </token>\n";
					continue;
				}
				
			}
			else if((buffer.length()==1)&&(buffer.charAt(0)=='#'))//文件结束标志
			{
				sum++;
				Fileinformaintion+="    <token>\n";
				Fileinformaintion+="      <number>"+sum+"</number>\n";
				Fileinformaintion+="      <value>"+"#"+"</value>\n";
				Fileinformaintion+="      <type>"+"#"+"</type>\n";
				Fileinformaintion+="      <line>"+line+"</line>\n";
		    	Fileinformaintion+="      <valid>true</valid>\n";
		    	Fileinformaintion+="    </token>\n";
		    	continue;
			}
			
		}
		
	}
	
	//输出
	public void writefile(String value,String type) throws IOException
	{
		
		Fileinformaintion+="      <number>"+sum+"</number>\n";
		Fileinformaintion+="      <value>"+value+"</value>\n";
		Fileinformaintion+="      <type>"+type+"</type>\n";
		Fileinformaintion+="      <line>"+line+"</line>\n";
    	Fileinformaintion+="      <valid>true</valid>\n";
    	Fileinformaintion+="    </token>\n";
	}
//	public static void main(String args[]){
//		System.out.println(args[0]);
//		
//	}
	
	//@Override
	public void run(String iFile, String oFile) throws  Exception {
		// TODO Auto-generated method stub
		//System.out.println(iFile);
		//System.out.println(oFile);
		try {
		MiniCCScanner scanning=new MiniCCScanner();
		String write=scanning.readfile(iFile);
		//System.out.println(write);
		MiniCCUtil.createAndWriteFile(oFile,write);
		System.out.println("2.词法分析完成！");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
