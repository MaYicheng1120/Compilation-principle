package bit.minisys.minicc.codegen;

import bit.minisys.minicc.symbol.SymbolEntryFunc;
import bit.minisys.minicc.symbol.SymbolTable;
import bit.minisys.minicc.tree.TreeNode;
import bit.minisys.minicc.tree.TreeNodeType;

public class genforCode {
		private String f1="for1";
		private String f2="for2";
		private String f3="for3";
		private String f4="for4";
		private int flag=0;
		private int endfor=0;
		private String time;
		public String GenforCode(TreeNode root, SymbolTable st, SymbolEntryFunc sef,MIPS mips,String times)
		{
			this.time=times;
			String code="";
			this.f1+=times.charAt(0);
			this.f2+=times.charAt(0);
			this.f3+=times.charAt(0);
			this.f4+=times.charAt(0);
			code+=genforCode(root, st,sef,mips);
			return code;
		}
		public String genforCode(TreeNode root, SymbolTable st, SymbolEntryFunc sef,MIPS mips){
		String code = "";
		TreeNodeType type = root.getType();
		boolean genNext=false;
		if((root.getName().equals("compoundStatement"))&&(root.getChildren().get(0).getName().equals("iterationStatement")))
		{
			genforCode for1=new genforCode();
			
			String a=time;
			Integer b=new Integer(a);
			b++;
			a=b.toString();
			for1.time=a;
			String t1,t2,t3,t4;
			t1=this.f1;
			t2=this.f2;
			t3=this.f3;
			t4=this.f4;
			for1.f1+=a;
			for1.f2+=a;
			for1.f3+=a;
			for1.f4+=a;
			code+=for1.genforCode(root.getChildren().get(0), st,sef,mips);
			this.f1=t1;
			this.f2=t2;
			this.f3=t3;
			this.f4=t4;
			this.flag=1;
			this.endfor=1;
			code+="\tj "+f4+"\n";
		}
		else if(type == TreeNodeType.TNT_ICONST)
		{
			code += "\tli " +mips.getRegName(root.getRegNum()) + ", ";
			String value = "" + root.getName();
			code += value + "\n";
			
		}//赋值语句  a=scanf
		else if(type == TreeNodeType.TNT_ASNMT_EXP)
		{
			TreeNode left = root.getByIndex(0);
			TreeNode right = root.getByIndex(2);
			//evaluation right
			if(root.getByIndex(2).getType() == TreeNodeType.TNT_CONST)
			{
				code+="\tli "+"$t0"+ ","+right.getName()+"\n";
				code+="\tmove " + mips.getRegName(left.getRegNum()) + ","+"$t0\n";
				
			}
			else
			{
				code += genforCode(root.getByIndex(2), st, sef, mips);
				//generate 
			
				if(left.getRegNum() != right.getRegNum()){
					code += "\tmove "+mips.getRegName(left.getRegNum()) + ", " +mips.getRegName(right.getRegNum()) + "\n";
				}
			}
			if(this.flag==0)
			{
				code+="\n"+f1+":\n";
				this.flag=1;
				
			}
			else if(this.flag==1)
			{
				code+="\tj "+f4+"\n";
				this.flag=0;
			}
			
			
		}//ADD a+b
		else if(type == TreeNodeType.TNT_ADD_EXP)
		{
			TreeNode op1 = root.getByIndex(0);
			TreeNode op2 = root.getByIndex(2);
			
			String opc = "";
			String reg1 = "";
			String reg2 = "";
			String reg3 = "";
			
			//求操作数
			code += genforCode(op1, st, sef, mips);
			code += genforCode(op2, st, sef, mips);
			
			reg1=mips.getRegName(root.getRegNum());
			if(op1.getType() == TreeNodeType.TNT_CONST && op2.getType() == TreeNodeType.TNT_CONST)
			{
				code += "\taddi " + reg1 + ", $zero, " + op1.getName() + "\n"; 
				reg2 = reg1;
				
			}
			else if(op1.getType() == TreeNodeType.TNT_CONST)
			{
				reg2 =mips.getRegName(op2.getRegNum());
				reg3 = op1.getName();
			}
			else if(op2.getType() == TreeNodeType.TNT_CONST)
			{
				reg2 =mips.getRegName(op1.getRegNum());
				reg3 = op2.getName();
			}
			else
			{
				reg2 =mips.getRegName(op1.getRegNum());
				reg3 =mips.getRegName(op2.getRegNum());
			}
			
			if(op1.getType() == TreeNodeType.TNT_CONST || op2.getType() == TreeNodeType.TNT_CONST)
			{
				if(root.getByIndex(1).getName().equals("+")){
					code += "\taddi ";
				}else if(root.getByIndex(1).getName().equals("-")){
					code += "\tsubi ";
				} 
			}
			else
			{
				if(root.getByIndex(1).getName().equals("+")){
					code += "\tadd ";
				}else if(root.getByIndex(1).getName().equals("-")){
					code += "\tsub ";
				}
			}

			if(type == TreeNodeType.TNT_ADD_EXP){
				code += reg1 + ", " + reg2 + ", " + reg3 + "\n";
			}
		}
		//JMP
		else if(type == TreeNodeType.TNT_JMP_STMT)
		{
			TreeNode expr = root.getByIndex(1);
			code += genforCode(expr, st, sef, mips);
			if(expr.getChildren().size() == 1 && expr.getByIndex(0).getType() == TreeNodeType.TNT_CONST){
				code += "\taddi $v0, $v0, " + expr.getByIndex(0).getName() + "\n";
			}else{
				code += "\tmove $v0, " +mips.getRegName(expr.getRegNum()) + "\n";
			}
		}   //i++   i--
		else if(type==TreeNodeType.TNT_POST_EXP)
		{
			if(root.getByIndex(1).getName().equals("++")){
				code+="\taddi "+mips.getRegName(root.getByIndex(0).getRegNum())+","
				+mips.getRegName(root.getByIndex(0).getRegNum())+",1"+"\n";
			}
			if(root.getByIndex(1).getName().equals("--")){
				code+="\taddi "+mips.getRegName(root.getByIndex(0).getRegNum())+","
				+mips.getRegName(root.getByIndex(0).getRegNum())+",-1"+"\n";
			}
			code+="\tj   "+f1+"\n";
			code+="\n"+f2+":\n";
		}  
		/*判断a>0   a<0  大于号："&gt;" 小于号："&lt;" 等于号：==
		  大于等于："&gt;="   小于等于："&lt;="
		  beq	$t0,$t1,target	#  branch to target if  $t0 = $t1
		blt	$t0,$t1,target	#  branch to target if  $t0 < $t1
		ble	$t0,$t1,target	#  branch to target if  $t0 <= $t1
		bgt	$t0,$t1,target	#  branch to target if  $t0 > $t1
		bge	$t0,$t1,target	#  branch to target if  $t0 >= $t1*/
		else if(type==TreeNodeType.TNT_REL_EXP)
		{
			TreeNode op1 = root.getByIndex(0);
			TreeNode op2 = root.getByIndex(2);
			String number1="";
			String number2="";
			if((op1.getType() == TreeNodeType.TNT_CONST)&&(op2.getType() == TreeNodeType.TNT_CONST))
			{
				number1=op1.getName();
				number2=op2.getName();
			}
			else if(op1.getType() == TreeNodeType.TNT_CONST)
			{
				number1=op1.getName();
				number2=mips.getRegName(op2.getRegNum());
			}
			else if(op2.getType() == TreeNodeType.TNT_CONST)
			{
				number1=mips.getRegName(op1.getRegNum());
				number2=op2.getName();
			}
			else
			{
				number1=mips.getRegName(op1.getRegNum());
				number2=mips.getRegName(op2.getRegNum());
			}
			if(root.getByIndex(1).getName().equals(">"))//大于
			{
				//bgt	$t0,$t1,target	#  branch to target if  $t0 > $t1
				//ble	$t0,$t1,target	#  branch to target if  $t0 < $t1小等于于跳出循环或分支语句，大于则继续
				//bgt	$t0,$t1,target	#  branch to target if  $t0 > $t1
				code+="\tbgt "+number1+","+number2+","+f2+"\n";
				code+="\tble "+number1+","+number2+","+f3+"\n";
				code+="\n"+f4+":\n";
			}
			if(root.getByIndex(1).getName().equals("<"))//小于
			{
				//bge	$t0,$t1,target	#  branch to target if  $t0 >= $t1
				//blt	$t0,$t1,target	#  branch to target if  $t0 < $t1大于等于于跳出循环或分支语句，小于则继续
				code+="\tblt "+number1+","+number2+","+f2+"\n";
				code+="\tbge "+number1+","+number2+","+f3+"\n";
				code+="\n"+f4+":\n";
			}
			if(root.getByIndex(1).getName().equals("<="))//小于等于
			{
				//bgt	$t0,$t1,target	#  branch to target if  $t0 > $t1
				//ble	$t0,$t1,target	#  branch to target if  $t0 <= $t1大于跳出循环或分支语句，小于等于则继续
				code+="\tble "+number1+","+number2+","+f2+"\n";
				code+="\tbgt "+number1+","+number2+","+f3+"\n";
				code+="\n"+f4+":\n";
			}
			if(root.getByIndex(1).getName().equals(">="))//大于等于
			{
				//bgt	$t0,$t1,target	#  branch to target if  $t0 > $t1
				//blt	$t0,$t1,target	#  branch to target if  $t0 < $t1小于跳出循环或分支语句，大于等于则继续
				//bge	$t0,$t1,target	#  branch to target if  $t0 >= $t1
				code+="\tbge "+number1+","+number2+","+f2+"\n";
				code+="\tblt "+number1+","+number2+","+f3+"\n";
				code+="\n"+f4+":\n";
			}
		}
		else
		{
			genNext = true;
		}
		
		if(genNext)
		{
			for(TreeNode c: root.getChildren()){
				code += genforCode(c, st, sef, mips);
			}
		}
		if(root.getType() == TreeNodeType.TNT_UNKNOWN){
			code+="\n"+f3+":\n";
		}
		
		return code;
	}
}
