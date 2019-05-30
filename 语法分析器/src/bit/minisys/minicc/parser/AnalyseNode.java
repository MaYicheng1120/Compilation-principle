package bit.minisys.minicc.parser;

import java.util.ArrayList;

public class AnalyseNode {
	 String type;
	 String name;
	 String value;
	 AnalyseNode firstChild;
	 AnalyseNode nextSubling;
	 boolean hasDealt;
	 String wordType;
	 static final String NONTERMINAL = "nonterminal";		//非终结符
	 static final String TERMINAL = "terminal";			//终结符
	 static final String END = "#";						//文件结束标志			                                        		//������
	

	public AnalyseNode(){}

	public AnalyseNode(String type, String name, String value, String wordType)
	{
		this.type = type;
		this.name = name;
		this.value = value;
		this.hasDealt = false;
		this.wordType = wordType;
	}
	public AnalyseNode(AnalyseNode node) {
		this.type = node.type;
		this.name = node.name;
		this.value = node.value;
		this.hasDealt = node.hasDealt;
		this.wordType = node.wordType;
	}

}


