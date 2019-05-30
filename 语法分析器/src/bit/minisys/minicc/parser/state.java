package bit.minisys.minicc.parser;

public class state {
   	AnalyseNode root;//根节点
	AnalyseNode PROGRAM=new AnalyseNode("nonterminal", "PROGRAM",null,null);//程序
	AnalyseNode FUNCTIONS= new AnalyseNode("nonterminal", "FUNCTIONS",null,null);	//函数
	AnalyseNode FUNLIST= new AnalyseNode("nonterminal", "FUNLIST",null,null);	//函数序列
	AnalyseNode FUNCTION= new AnalyseNode("nonterminal", "FUNCTION",null,null); 	//函数声明
	AnalyseNode ARGS= new AnalyseNode("nonterminal", "ARGS",null,null);//参数
	AnalyseNode ALIST= new AnalyseNode("nonterminal", "ALIST",null,null);		//参数序列
	AnalyseNode FARGS= new AnalyseNode("nonterminal", "FARGS",null,null);		//
	AnalyseNode FUNC_BODY= new AnalyseNode("nonterminal", "FUNC_BODY",null,null);	//函数体
	AnalyseNode STMTS= new AnalyseNode("nonterminal", "STMTS",null,null);		//陈述序列
   	AnalyseNode STMT= new AnalyseNode("nonterminal", "STMT",null,null);		//陈述语句
   	AnalyseNode EXPR_STMT= new AnalyseNode("nonterminal", "EXPR_STMT",null,null);	//表达式语句
   	AnalyseNode RET_STMT= new AnalyseNode("nonterminal", "RET_STMT",null,null);	//return返回语句
   	AnalyseNode FOR_STMT= new AnalyseNode("nonterminal", "FOR_STMT",null,null);	//for循环语句
   	AnalyseNode IF_STMT= new AnalyseNode("nonterminal", "IF_STMT",null,null);	//if选择语句
   	AnalyseNode EXPR= new AnalyseNode("nonterminal", "EXPR",null,null);		//表达式
   	AnalyseNode FACTOR= new AnalyseNode("nonterminal", "FACTOR",null,null);		//修饰符
   	AnalyseNode FLIST= new AnalyseNode("nonterminal", "FLIST",null,null);		//表达式列表
   	AnalyseNode EARGS= new AnalyseNode("nonterminal", "EARGS",null,null);		//参数声明
   	AnalyseNode EALIST= new AnalyseNode("nonterminal", "EALIST",null,null);		//参数声明序列
   	AnalyseNode ELSEIF= new AnalyseNode("nonterminal", "ELSEIF",null,null);		//else-if语句
   	AnalyseNode ILIST= new AnalyseNode("nonterminal", "ILIST",null,null);		//if语句序列
   	AnalyseNode TYPE= new AnalyseNode("nonterminal", "TYPE",null,null);		//数据类型
   	
   	//终结符
   	AnalyseNode TKN_ID= new AnalyseNode("terminal", "id",null, "GiG");		//标识符
   	AnalyseNode TKN_CONST_I= new AnalyseNode("terminal",null,"const_i", "GjG");//常量
   	AnalyseNode TKN_LP= new AnalyseNode("terminal", "(",null, "separator");		//左括号
   	AnalyseNode TKN_RP= new AnalyseNode("terminal", ")",null, "separator");		//右括号
   	AnalyseNode TKN_COMMA= new AnalyseNode("terminal", ",",null, "separator");	//逗号
   	AnalyseNode TKN_LB= new AnalyseNode("terminal", "{",null, "separator");		//左大括号
   	AnalyseNode TKN_RB= new AnalyseNode("terminal", "}",null, "separator");		//右大括号
   	AnalyseNode TKN_SEMICOLON= new AnalyseNode("terminal", ";",null, "separator");//分号
   	AnalyseNode TKN_KW_RET= new AnalyseNode("terminal", "return",null, "keyword");	//关键字return
   	AnalyseNode TKN_PLUS= new AnalyseNode("terminal", "+",null, "operator");	//加等
   	AnalyseNode TKN_MINUS= new AnalyseNode("terminal", "-",null, "operator");	//负号
   	AnalyseNode TKN_LESS= new AnalyseNode("terminal", "<",null, "operator");	//小于
   	AnalyseNode TKN_DIV= new AnalyseNode("terminal", "/",null, "operator");	//减
   	AnalyseNode TKN_ASN= new AnalyseNode("terminal", "=",null, "operator");	//等于
   	AnalyseNode TKN_INT= new AnalyseNode("terminal", "int",null, "keyword");	//int
   	AnalyseNode TKN_FLOAT= new AnalyseNode("terminal", "float",null, "keyword");	//float
   	AnalyseNode TKN_FOR= new AnalyseNode("terminal", "for",null, "keyword");	//for
   	AnalyseNode TKN_IF= new AnalyseNode("terminal", "if",null, "keyword");		//if
   	AnalyseNode TKN_ELSE= new AnalyseNode("terminal", "else",null, "keyword");	//else
   	AnalyseNode TKN_END= new AnalyseNode("terminal", "#",null, "#");	//#
}
