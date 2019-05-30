#define NUM 4

/* this is a demo program */

int main(){
	int a;
	int b;
	int i;
	int j;
	int k;
	a = MARS_SCANF_I();
	b = MARS_SCANF_I();
	if(b>100)
	{
		b=1;
	}
	for(i=1;i<=3;i++)
	{
		for(j=1;j<=3;j++)
		{
			for(k=1;k<=2;k++)
			{
				a = a + b;
			}
		}
	}
	
	if(a>50)
	{
		a=0;
	}
	
	MARS_PRINTF_I(a);
	return 0;
}
