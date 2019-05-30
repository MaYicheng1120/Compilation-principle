/*    */ package bit.minisys.minicc.parser;
/*    */ 
/*    */ public class Table
/*    */ {
/* 12 */   String[] PROGRAM = { "FUNCTIONS" };
/* 13 */   String[] FUNCTIONS = { "FUNCTION FUNLIST" };
/* 14 */   String[] FUNLIST = { "FUNCTIONS", "NULL" };
/*    */ 
/* 16 */   String[] FUNCTION = { "TYPE TKN_ID TKN_LP ARGS TKN_RP FUNC_BODY" };
/* 17 */   String[] ARGS = { "FARGS ALIST", "NULL" };
/* 18 */   String[] FARGS = { "TYPE TKN_ID" };
/* 19 */   String[] ALIST = { "TKN_COMMA FARGS ALIST", "NULL" };
/*    */ 
/* 21 */   String[] FUNC_BODY = { "TKN_LB STMTS TKN_RB" };
/* 22 */   String[] STMTS = { "STMT STMTS", "NULL" };
/* 23 */   String[] STMT = { "EXPR_STMT", "RET_STMT", "FOR_STMT", "IF_STMT" };
/*    */ 
/* 25 */   String[] EXPR_STMT = { "EXPR TKN_SEMICOLON" };
/* 26 */   String[] EXPR = { "FACTOR FLIST", "EARGS" };
/* 27 */   String[] FACTOR = { "TKN_LP EXPR TKN_RP", "TKN_ID", "TKN_CONST_I" };
/* 28 */   String[] FLIST = { "TKN_ASN FACTOR FLIST", "TKN_PLUS FACTOR FLIST", "TKN_LESS FACTOR FLIST", "NULL" };
/* 29 */   String[] EARGS = { "FARGS EALIST", "NULL" };
/* 30 */   String[] EALIST = { "TKN_COMMA TKN_ID EALIST", "NULL" };
/*    */ 
/* 32 */   String[] RET_STMT = { "TKN_KW_RET EXPR_STMT" };
/*    */ 
/* 34 */   String[] FOR_STMT = { "TKN_FOR TKN_LP EXPR_STMT EXPR_STMT EXPR TKN_RP TKN_LB STMTS TKN_RB" };
/*    */ 
/* 36 */   String[] IF_STMT = { "TKN_IF TKN_LP EXPR TKN_RP TKN_LB STMTS TKN_RB ELSEIF" };
/* 37 */   String[] ELSEIF = { "TKN_ELSE ILIST", "RET_STMT","NULL" };
/* 38 */   String[] ILIST = { "IF_STMT", "TKN_LB STMTS TKN_RB" };
/*    */ 
/* 40 */   String[] TYPE = { "TKN_INT", "TKN_FLOAT" };
/*    */ }

/* Location:           C:\Users\高子恺\Desktop\2017编译原理与设计\bit-minic-compiler-master\run\BITMiniCC.jar
 * Qualified Name:     bit.minisys.minicc.parser.Table
 * JD-Core Version:    0.6.0
 */