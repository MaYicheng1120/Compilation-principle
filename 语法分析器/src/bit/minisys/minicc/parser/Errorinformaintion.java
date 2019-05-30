package bit.minisys.minicc.parser;

public class Errorinformaintion {
	
		int sumerror;
		String errortype;
		int line;
		String errorword;
		public Errorinformaintion(int sumerror,String errortype,int line,String errorword)
		{
			this.sumerror=sumerror;
			this.errortype=errortype;
			this.line=line;
			this.errorword=errorword;
		}
		
		
		
}
