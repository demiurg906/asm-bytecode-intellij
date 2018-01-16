package com.thiakil.idea;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.psi.ASMToken;
import com.intellij.psi.TokenType;
import com.intellij.psi.JavaTokenType;

%%

%{
    public ASMTextifiedLexer(){
        this(null);
    }
%}

%class ASMTextifiedLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE_CHAR = [\ \n\r\t\f]

IDENTIFIER = [:jletter:] [:jletterdigit:]*

C_STYLE_COMMENT=("/*"[^"*"]{COMMENT_TAIL})|"/*"
//DOC_COMMENT="/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
END_OF_LINE_COMMENT="/""/"[^\r\n]*

DIGIT = [0-9]
DIGIT_OR_UNDERSCORE = [_0-9]
DIGITS = {DIGIT} | {DIGIT} {DIGIT_OR_UNDERSCORE}*
HEX_DIGIT_OR_UNDERSCORE = [_0-9A-Fa-f]

INTEGER_LITERAL = {DIGITS} | {HEX_INTEGER_LITERAL} | {BIN_INTEGER_LITERAL}
LONG_LITERAL = {INTEGER_LITERAL} [Ll]
HEX_INTEGER_LITERAL = 0 [Xx] {HEX_DIGIT_OR_UNDERSCORE}*
BIN_INTEGER_LITERAL = 0 [Bb] {DIGIT_OR_UNDERSCORE}*

FLOAT_LITERAL = ({DEC_FP_LITERAL} | {HEX_FP_LITERAL}) [Ff] | {DIGITS} [Ff]
DOUBLE_LITERAL = ({DEC_FP_LITERAL} | {HEX_FP_LITERAL}) [Dd]? | {DIGITS} [Dd]
DEC_FP_LITERAL = {DIGITS} {DEC_EXPONENT} | {DEC_SIGNIFICAND} {DEC_EXPONENT}?
DEC_SIGNIFICAND = "." {DIGITS} | {DIGITS} "." {DIGIT_OR_UNDERSCORE}*
DEC_EXPONENT = [Ee] [+-]? {DIGIT_OR_UNDERSCORE}*
HEX_FP_LITERAL = {HEX_SIGNIFICAND} {HEX_EXPONENT}
HEX_SIGNIFICAND = 0 [Xx] ({HEX_DIGIT_OR_UNDERSCORE}+ "."? | {HEX_DIGIT_OR_UNDERSCORE}* "." {HEX_DIGIT_OR_UNDERSCORE}+)
HEX_EXPONENT = [Pp] [+-]? {DIGIT_OR_UNDERSCORE}*

ESCAPE_SEQUENCE = \\[^\r\n]
CHARACTER_LITERAL = "'" ([^\\\'\r\n] | {ESCAPE_SEQUENCE})* ("'"|\\)?
STRING_LITERAL = \" ([^\\\"\r\n] | {ESCAPE_SEQUENCE})* (\"|\\)?

ASM_INSTRUCTION=(NOP|ACONST_NULL|ICONST_M1|ICONST_0|ICONST_1|ICONST_2|ICONST_3|ICONST_4|ICONST_5|LCONST_0|LCONST_1|FCONST_0|FCONST_1|FCONST_2|DCONST_0|DCONST_1|BIPUSH|SIPUSH|LDC|ILOAD|LLOAD|FLOAD|DLOAD|ALOAD|IALOAD|LALOAD|FALOAD|DALOAD|AALOAD|BALOAD|CALOAD|SALOAD|ISTORE|LSTORE|FSTORE|DSTORE|ASTORE|IASTORE|LASTORE|FASTORE|DASTORE|AASTORE|BASTORE|CASTORE|SASTORE|POP|POP2|DUP|DUP_X1|DUP_X2|DUP2|DUP2_X1|DUP2_X2|SWAP|IADD|LADD|FADD|DADD|ISUB|LSUB|FSUB|DSUB|IMUL|LMUL|FMUL|DMUL|IDIV|LDIV|FDIV|DDIV|IREM|LREM|FREM|DREM|INEG|LNEG|FNEG|DNEG|ISHL|LSHL|ISHR|LSHR|IUSHR|LUSHR|IAND|LAND|IOR|LOR|IXOR|LXOR|IINC|I2L|I2F|I2D|L2I|L2F|L2D|F2I|F2L|F2D|D2I|D2L|D2F|I2B|I2C|I2S|LCMP|FCMPL|FCMPG|DCMPL|DCMPG|IFEQ|IFNE|IFLT|IFGE|IFGT|IFLE|IF_ICMPEQ|IF_ICMPNE|IF_ICMPLT|IF_ICMPGE|IF_ICMPGT|IF_ICMPLE|IF_ACMPEQ|IF_ACMPNE|GOTO|JSR|RET|TABLESWITCH|LOOKUPSWITCH|IRETURN|LRETURN|FRETURN|DRETURN|ARETURN|RETURN|GETSTATIC|PUTSTATIC|GETFIELD|PUTFIELD|INVOKEVIRTUAL|INVOKESPECIAL|INVOKESTATIC|INVOKEINTERFACE|INVOKEDYNAMIC|NEW|NEWARRAY|ANEWARRAY|ARRAYLENGTH|ATHROW|CHECKCAST|INSTANCEOF|MONITORENTER|MONITOREXIT|MULTIANEWARRAY|IFNULL|IFNONNULL)
LABEL = L[0-9]
DESC_ELEMENT = ([BCDFIJSZ]|L([a-z_0-9\$/]+);)
DESC = (\[)*{DESC_ELEMENT}
METHOD_DESC = \({DESC}*\)({DESC}|V)
CLASS_REF = ([a-z0-9_\$]\/)+[a-z0-9_\$]

//%state

%%

<YYINITIAL> {

  {WHITE_SPACE_CHAR}+ { return JavaTokenType.WHITE_SPACE; }

  {C_STYLE_COMMENT} { return JavaTokenType.C_STYLE_COMMENT; }
  {END_OF_LINE_COMMENT} { return JavaTokenType.END_OF_LINE_COMMENT; }
  //{DOC_COMMENT} { return JavaDocElementType.DOC_COMMENT; }

  {LONG_LITERAL} { return JavaTokenType.LONG_LITERAL; }
  {INTEGER_LITERAL} { return JavaTokenType.INTEGER_LITERAL; }
  {FLOAT_LITERAL} { return JavaTokenType.FLOAT_LITERAL; }
  {DOUBLE_LITERAL} { return JavaTokenType.DOUBLE_LITERAL; }
  {CHARACTER_LITERAL} { return JavaTokenType.CHARACTER_LITERAL; }
  {STRING_LITERAL} { return JavaTokenType.STRING_LITERAL; }

  "true" { return JavaTokenType.TRUE_KEYWORD; }
  "false" { return JavaTokenType.FALSE_KEYWORD; }
  "null" { return JavaTokenType.NULL_KEYWORD; }

  "abstract" { return JavaTokenType.ABSTRACT_KEYWORD; }
  //"assert" { return JavaTokenType.ASSERT_KEYWORD; }
  //"boolean" { return JavaTokenType.BOOLEAN_KEYWORD; }
  //"break" { return JavaTokenType.BREAK_KEYWORD; }
  "byte" { return JavaTokenType.BYTE_KEYWORD; }
  //"case" { return JavaTokenType.CASE_KEYWORD; }
  //"catch" { return JavaTokenType.CATCH_KEYWORD; }
  "char" { return JavaTokenType.CHAR_KEYWORD; }
  "class" { return JavaTokenType.CLASS_KEYWORD; }
  "const" { return JavaTokenType.CONST_KEYWORD; }
  //"continue" { return JavaTokenType.CONTINUE_KEYWORD; }
  //"default" { return JavaTokenType.DEFAULT_KEYWORD; }
  //"do" { return JavaTokenType.DO_KEYWORD; }
  "double" { return JavaTokenType.DOUBLE_KEYWORD; }
  //"else" { return JavaTokenType.ELSE_KEYWORD; }
  "enum" { return JavaTokenType.ENUM_KEYWORD; }
  "extends" { return JavaTokenType.EXTENDS_KEYWORD; }
  "final" { return JavaTokenType.FINAL_KEYWORD; }
  //"finally" { return JavaTokenType.FINALLY_KEYWORD; }
  //"float" { return JavaTokenType.FLOAT_KEYWORD; }
  //"for" { return JavaTokenType.FOR_KEYWORD; }
  //"goto" { return JavaTokenType.GOTO_KEYWORD; }
  //"if" { return JavaTokenType.IF_KEYWORD; }
  "implements" { return JavaTokenType.IMPLEMENTS_KEYWORD; }
  //"import" { return JavaTokenType.IMPORT_KEYWORD; }
  //"instanceof" { return JavaTokenType.INSTANCEOF_KEYWORD; }
  //"int" { return JavaTokenType.INT_KEYWORD; }
  "interface" { return JavaTokenType.INTERFACE_KEYWORD; }
  //"long" { return JavaTokenType.LONG_KEYWORD; }
  "native" { return JavaTokenType.NATIVE_KEYWORD; }
  //"new" { return JavaTokenType.NEW_KEYWORD; }
  "package" { return JavaTokenType.PACKAGE_KEYWORD; }
  "private" { return JavaTokenType.PRIVATE_KEYWORD; }
  "public" { return JavaTokenType.PUBLIC_KEYWORD; }
  "short" { return JavaTokenType.SHORT_KEYWORD; }
  "super" { return JavaTokenType.SUPER_KEYWORD; }
  //"switch" { return JavaTokenType.SWITCH_KEYWORD; }
  "synchronized" { return JavaTokenType.SYNCHRONIZED_KEYWORD; }
  "this" { return JavaTokenType.THIS_KEYWORD; }
  //"throw" { return JavaTokenType.THROW_KEYWORD; }
  "protected" { return JavaTokenType.PROTECTED_KEYWORD; }
  "transient" { return JavaTokenType.TRANSIENT_KEYWORD; }
  //"return" { return JavaTokenType.RETURN_KEYWORD; }
  //"void" { return JavaTokenType.VOID_KEYWORD; }
  "static" { return JavaTokenType.STATIC_KEYWORD; }
  "strictfp" { return JavaTokenType.STRICTFP_KEYWORD; }
  //"while" { return JavaTokenType.WHILE_KEYWORD; }
  //"try" { return JavaTokenType.TRY_KEYWORD; }
  "volatile" { return JavaTokenType.VOLATILE_KEYWORD; }
  "throws" { return JavaTokenType.THROWS_KEYWORD; }

  {ASM_INSTRUCTION} { return ASMToken.INSTRUCTION; }

  {METHOD_DESC} { return ASMToken.METHOD_DESC; }

  {DESC} { return ASMToken.DESC; }

  {LABEL} { return ASMToken.LABEL_DECLARATION; }

  {CLASS_REF} { return ASMToken.CLASS_NAME; }

  {IDENTIFIER} { return JavaTokenType.IDENTIFIER; }

  "==" { return JavaTokenType.EQEQ; }
  "!=" { return JavaTokenType.NE; }
  "||" { return JavaTokenType.OROR; }
  "++" { return JavaTokenType.PLUSPLUS; }
  "--" { return JavaTokenType.MINUSMINUS; }

  "<" { return JavaTokenType.LT; }
  "<=" { return JavaTokenType.LE; }
  "<<=" { return JavaTokenType.LTLTEQ; }
  "<<" { return JavaTokenType.LTLT; }
  ">" { return JavaTokenType.GT; }
  "&" { return JavaTokenType.AND; }
  "&&" { return JavaTokenType.ANDAND; }

  "+=" { return JavaTokenType.PLUSEQ; }
  "-=" { return JavaTokenType.MINUSEQ; }
  "*=" { return JavaTokenType.ASTERISKEQ; }
  "/=" { return JavaTokenType.DIVEQ; }
  "&=" { return JavaTokenType.ANDEQ; }
  "|=" { return JavaTokenType.OREQ; }
  "^=" { return JavaTokenType.XOREQ; }
  "%=" { return JavaTokenType.PERCEQ; }

  "("   { return JavaTokenType.LPARENTH; }
  ")"   { return JavaTokenType.RPARENTH; }
  "{"   { return JavaTokenType.LBRACE; }
  "}"   { return JavaTokenType.RBRACE; }
  "["   { return JavaTokenType.LBRACKET; }
  "]"   { return JavaTokenType.RBRACKET; }
  ";"   { return JavaTokenType.SEMICOLON; }
  ","   { return JavaTokenType.COMMA; }
  "..." { return JavaTokenType.ELLIPSIS; }
  "."   { return JavaTokenType.DOT; }

  "=" { return JavaTokenType.EQ; }
  "!" { return JavaTokenType.EXCL; }
  "~" { return JavaTokenType.TILDE; }
  "?" { return JavaTokenType.QUEST; }
  ":" { return JavaTokenType.COLON; }
  "+" { return JavaTokenType.PLUS; }
  "-" { return JavaTokenType.MINUS; }
  "*" { return JavaTokenType.ASTERISK; }
  "/" { return JavaTokenType.DIV; }
  "|" { return JavaTokenType.OR; }
  "^" { return JavaTokenType.XOR; }
  "%" { return JavaTokenType.PERC; }
  "@" { return JavaTokenType.AT; }

  //"::" { return JavaTokenType.DOUBLE_COLON; }
  //"->" { return JavaTokenType.ARROW; }
}

.                                                           { return TokenType.BAD_CHARACTER; }