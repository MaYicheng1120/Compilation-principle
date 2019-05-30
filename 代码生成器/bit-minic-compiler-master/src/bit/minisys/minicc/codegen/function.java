package bit.minisys.minicc.codegen;

import bit.minisys.minicc.tree.TreeNode;
import bit.minisys.minicc.tree.graphic.GraphicTree;
import bit.minisys.minicc.tree.graphic.TreeViewer;

public class function {
	
	public String genHeader(String iFile){
		String code = "";
		
		code += "\n";
		code += "# Code generated from MiniCCompiler at \n";
		code += "# input file:" + iFile + "\n";
		code += "# Please do not change this file! \n";
		code += "\n";
		
		return code;
	}
	public String genDataSection(){
		String code = "";
		
		code += "\t.data\n\n";
		
		return code;
	}
	
	public String genInitCode(){
		String code = "";
		
		code += "__init:\n";
		code += "\t# setup the stack\n";
		code += "\tlui $sp, 0x8000\n";
		code += "\taddi $sp, $sp, 0x0000\n";
		code += "\taddiu $sp, $sp, -1024\n";
		code += "\n";
		
		code += "\t# redirect to main function\n";
		code += "\tjal __main\n";
		code += "\n";
		
		code += "\t# make system call to terminate the program\n";
		code += "\tli $v0, 10\n";
		code += "\tsyscall\n";
		code += "\n";
		
		return code;
	}
	
}
