 package bit.minisys.minicc.scanner;
/**
 * �ж϶�ȡ���ַ�����
 * @author 1320151094 GJLDR
 */
public class judge {
	private final String keyWords[] = {"auto","break","case",
			"const","char","continue","default","do",
			"double","else","extern","enum","float","for",
			"if","inline","int","long","printf","register","restrict","return","short","signed","sizeof","static",
			"switch","struct","typedef","union","unsigned","void","volatile","while","_Alignas",
			"_Alignof","_Atomic","_Bool","_Complex","_Genertic","_Imaginary",
			"_Noreturn","_Static_assert","_Thread_local","void","while","for","scanf"}; // �ؼ�������
	private char operators[] = {'+','-','*','/','=','>','<','&'}; // ���������
	private char separators[] = {',',';','{','}','(',')','[',']','_',
			':','.','"',}; // �ָ�������
	
	
	//�ж��Ƿ�Ϊ��ĸ
	public boolean letter(char ch)
	{
		return Character.isLetter(ch);
	}
	//�ж��Ƿ�Ϊ����
	public boolean number(char ch)
	{
		return Character.isDigit(ch);
	}
	//�ж��Ƿ�Ϊ�ؼ���
	public boolean keyWord(String s)
	{
		for(int i=0;i<keyWords.length;i++)
		{
			if(keyWords[i].equals(s))
				return true;
		}
		return false;
	}
	//�ж��Ƿ�Ϊ�����
	public boolean operator(char ch){
		for (int i=0;i<operators.length;i++)
		{
			if(ch==operators[i])
				return true;
		}
		return false;
	}
	//�ж��Ƿ�Ϊ���޷�
	public boolean separators(char ch){
		for (int i=0;i< separators.length;i++)
		{
			if(ch==separators[i])
				return true;
		}
		return false;
	}
	

}
