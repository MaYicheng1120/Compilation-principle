package bit.minisys.minicc.parser;

import java.util.ArrayList;

public class Word {
	public int number;
	public String value;
	public String type;
	public int line;
	public boolean flag = true;
	public Word()
	  {
	  }
	public Word(int number, String value, String type, int line)
	{
		this.number=number;
	    this.value = value;
	    this.type = type;
	    this.line = line;
	}
}
