package com.thiakil.idea;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.thiakil.idea.psi.TextifiedAsmTypes.*;

%%

%{
  public _TextifiedAsmLexer() {
    this((java.io.Reader)null);
  }

  private boolean groupStateWaitingDouble = false;
%}

%public
%class _TextifiedAsmLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

COMMENT=("//".*|"/"\*(.|\s)*?\*"/")
REFERENCE_TYPE=(L([a-zA-Z\$_][a-zA-Z0-9\$_]*"/")*[a-zA-Z\$_][a-zA-Z0-9\$_]*?;)
REFERENCE_TYPE_ARRAY=\[+(L([a-zA-Z\$_][a-zA-Z0-9\$_]*"/")*[a-zA-Z\$_][a-zA-Z0-9\$_]*?;)
DESC_PRIMITIVE=\[*([BCDFIJSZV])
METHOD_DESC_TOKEN=\(\[*([BCDFIJSZ]|L([a-zA-Z\$_][a-zA-Z0-9\$_]*"/")*[a-zA-Z\$_][a-zA-Z0-9\$_]*?;)*\)\[*([BCDFIJSZV]|L([a-zA-Z\$_][a-zA-Z0-9\$_]*"/")*[a-zA-Z\$_][a-zA-Z0-9\$_]*?;)
LABEL_ID=(L[0-9]+)
TYPE_NAME=(([a-zA-Z\$_][a-zA-Z0-9\$_]*"/")+[a-zA-Z\$_][a-zA-Z0-9\$_]*)
IDENTIFIER=([a-zA-Z\$_][a-zA-Z0-9\$_]*)
NUMBER=(-?[0-9]+(\.[0-9]*)?)
STRING=('([^'\\]|\\.)*'|\"([^\"\\]|\\.)*\")

%xstate FRAME_INSN
%xstates WAITING_GROUP, INSIDE_GROUP
//%state INSIDE_GROUP

%%

<YYINITIAL,FRAME_INSN,WAITING_GROUP,INSIDE_GROUP>{
    {WHITE_SPACE}               { return WHITE_SPACE; }
    {EOL}                       { return EOL; }
    {COMMENT}                   { return COMMENT; }
}

<YYINITIAL> {
  "true"                      { return TRUE; }
  "false"                     { return FALSE; }
  "class"                     { return CLASS; }
  "INNERCLASS"                { return INNERCLASS; }
  "public"                    { return PUBLIC; }
  "abstract"                  { return ABSTRACT; }
  "final"                     { return FINAL; }
  "enum"                      { return ENUM; }
  "extends"                   { return EXTENDS; }
  "implements"                { return IMPLEMENTS; }
  "protected"                 { return PROTECTED; }
  "private"                   { return PRIVATE; }
  "static"                    { return STATIC; }
  "transient"                 { return TRANSIENT; }
  "volatile"                  { return VOLATILE; }
  "synthetic"                 { return SYNTHETIC; }
  "synchronized"              { return SYNCHRONIZED; }
  "native"                    { return NATIVE; }
  "bridge"                    { return BRIDGE; }
  "default"                   { return DEFAULT; }
  "LOCALVARIABLE"             { return LOCALVARIABLE; }
  "MAXSTACK"                  { return MAXSTACK; }
  "MAXLOCALS"                 { return MAXLOCALS; }
  "interface"                 { return INTERFACE; }
  "NOP"                       { return NOP; }
  "ACONST_NULL"               { return ACONST_NULL; }
  "ICONST_M1"                 { return ICONST_M1; }
  "ICONST_0"                  { return ICONST_0; }
  "ICONST_1"                  { return ICONST_1; }
  "ICONST_2"                  { return ICONST_2; }
  "ICONST_3"                  { return ICONST_3; }
  "ICONST_4"                  { return ICONST_4; }
  "ICONST_5"                  { return ICONST_5; }
  "LCONST_0"                  { return LCONST_0; }
  "LCONST_1"                  { return LCONST_1; }
  "FCONST_0"                  { return FCONST_0; }
  "FCONST_1"                  { return FCONST_1; }
  "FCONST_2"                  { return FCONST_2; }
  "DCONST_0"                  { return DCONST_0; }
  "DCONST_1"                  { return DCONST_1; }
  "IALOAD"                    { return IALOAD; }
  "LALOAD"                    { return LALOAD; }
  "FALOAD"                    { return FALOAD; }
  "DALOAD"                    { return DALOAD; }
  "AALOAD"                    { return AALOAD; }
  "BALOAD"                    { return BALOAD; }
  "CALOAD"                    { return CALOAD; }
  "SALOAD"                    { return SALOAD; }
  "IASTORE"                   { return IASTORE; }
  "LASTORE"                   { return LASTORE; }
  "FASTORE"                   { return FASTORE; }
  "DASTORE"                   { return DASTORE; }
  "AASTORE"                   { return AASTORE; }
  "BASTORE"                   { return BASTORE; }
  "CASTORE"                   { return CASTORE; }
  "SASTORE"                   { return SASTORE; }
  "POP"                       { return POP; }
  "POP2"                      { return POP2; }
  "DUP"                       { return DUP; }
  "DUP_X1"                    { return DUP_X1; }
  "DUP_X2"                    { return DUP_X2; }
  "DUP2"                      { return DUP2; }
  "DUP2_X1"                   { return DUP2_X1; }
  "DUP2_X2"                   { return DUP2_X2; }
  "SWAP"                      { return SWAP; }
  "IADD"                      { return IADD; }
  "LADD"                      { return LADD; }
  "FADD"                      { return FADD; }
  "DADD"                      { return DADD; }
  "ISUB"                      { return ISUB; }
  "LSUB"                      { return LSUB; }
  "FSUB"                      { return FSUB; }
  "DSUB"                      { return DSUB; }
  "IMUL"                      { return IMUL; }
  "LMUL"                      { return LMUL; }
  "FMUL"                      { return FMUL; }
  "DMUL"                      { return DMUL; }
  "IDIV"                      { return IDIV; }
  "LDIV"                      { return LDIV; }
  "FDIV"                      { return FDIV; }
  "DDIV"                      { return DDIV; }
  "IREM"                      { return IREM; }
  "LREM"                      { return LREM; }
  "FREM"                      { return FREM; }
  "DREM"                      { return DREM; }
  "INEG"                      { return INEG; }
  "LNEG"                      { return LNEG; }
  "FNEG"                      { return FNEG; }
  "DNEG"                      { return DNEG; }
  "ISHL"                      { return ISHL; }
  "LSHL"                      { return LSHL; }
  "ISHR"                      { return ISHR; }
  "LSHR"                      { return LSHR; }
  "IUSHR"                     { return IUSHR; }
  "LUSHR"                     { return LUSHR; }
  "IAND"                      { return IAND; }
  "LAND"                      { return LAND; }
  "IOR"                       { return IOR; }
  "LOR"                       { return LOR; }
  "IXOR"                      { return IXOR; }
  "LXOR"                      { return LXOR; }
  "I2L"                       { return I2L; }
  "I2F"                       { return I2F; }
  "I2D"                       { return I2D; }
  "L2I"                       { return L2I; }
  "L2F"                       { return L2F; }
  "L2D"                       { return L2D; }
  "F2I"                       { return F2I; }
  "F2L"                       { return F2L; }
  "F2D"                       { return F2D; }
  "D2I"                       { return D2I; }
  "D2L"                       { return D2L; }
  "D2F"                       { return D2F; }
  "I2B"                       { return I2B; }
  "I2C"                       { return I2C; }
  "I2S"                       { return I2S; }
  "LCMP"                      { return LCMP; }
  "FCMPL"                     { return FCMPL; }
  "FCMPG"                     { return FCMPG; }
  "DCMPL"                     { return DCMPL; }
  "DCMPG"                     { return DCMPG; }
  "IRETURN"                   { return IRETURN; }
  "LRETURN"                   { return LRETURN; }
  "FRETURN"                   { return FRETURN; }
  "DRETURN"                   { return DRETURN; }
  "ARETURN"                   { return ARETURN; }
  "RETURN"                    { return RETURN; }
  "ARRAYLENGTH"               { return ARRAYLENGTH; }
  "ATHROW"                    { return ATHROW; }
  "MONITORENTER"              { return MONITORENTER; }
  "MONITOREXIT"               { return MONITOREXIT; }
  "RET"                       { return RET; }
  "LINENUMBER"                { return LINENUMBER; }
  "NEW"                       { return NEW; }
  "LDC"                       { return LDC; }
  "ILOAD"                     { return ILOAD; }
  "LLOAD"                     { return LLOAD; }
  "FLOAD"                     { return FLOAD; }
  "DLOAD"                     { return DLOAD; }
  "ALOAD"                     { return ALOAD; }
  "ISTORE"                    { return ISTORE; }
  "LSTORE"                    { return LSTORE; }
  "FSTORE"                    { return FSTORE; }
  "DSTORE"                    { return DSTORE; }
  "ASTORE"                    { return ASTORE; }
  "INVOKESPECIAL"             { return INVOKESPECIAL; }
  "INVOKESTATIC"              { return INVOKESTATIC; }
  "INVOKEVIRTUAL"             { return INVOKEVIRTUAL; }
  "INVOKEINTERFACE"           { return INVOKEINTERFACE; }
  "INVOKEDYNAMIC"             { return INVOKEDYNAMIC; }
  "BIPUSH"                    { return BIPUSH; }
  "SIPUSH"                    { return SIPUSH; }
  "PUTFIELD"                  { return PUTFIELD; }
  "GETFIELD"                  { return GETFIELD; }
  "GETSTATIC"                 { return GETSTATIC; }
  "PUTSTATIC"                 { return PUTSTATIC; }
  "ANEWARRAY"                 { return ANEWARRAY; }
  "NEWARRAY"                  { return NEWARRAY; }
  "T_BOOLEAN"                 { return T_BOOLEAN; }
  "T_CHAR"                    { return T_CHAR; }
  "T_FLOAT"                   { return T_FLOAT; }
  "T_DOUBLE"                  { return T_DOUBLE; }
  "T_BYTE"                    { return T_BYTE; }
  "T_SHORT"                   { return T_SHORT; }
  "T_INT"                     { return T_INT; }
  "T_LONG"                    { return T_LONG; }
  "TRYCATCHBLOCK"             { return TRYCATCHBLOCK; }
  "IFEQ"                      { return IFEQ; }
  "IFNE"                      { return IFNE; }
  "IFLT"                      { return IFLT; }
  "IFGE"                      { return IFGE; }
  "IFGT"                      { return IFGT; }
  "IFLE"                      { return IFLE; }
  "IF_ICMPEQ"                 { return IF_ICMPEQ; }
  "IF_ICMPNE"                 { return IF_ICMPNE; }
  "IF_ICMPLT"                 { return IF_ICMPLT; }
  "IF_ICMPGE"                 { return IF_ICMPGE; }
  "IF_ICMPGT"                 { return IF_ICMPGT; }
  "IF_ICMPLE"                 { return IF_ICMPLE; }
  "IF_ACMPEQ"                 { return IF_ACMPEQ; }
  "IF_ACMPNE"                 { return IF_ACMPNE; }
  "GOTO"                      { return GOTO; }
  "JSR"                       { return JSR; }
  "IFNULL"                    { return IFNULL; }
  "IFNONNULL"                 { return IFNONNULL; }
  "FRAME"                     { yybegin(FRAME_INSN); return FRAME; }
  "IINC"                      { return IINC; }
  "INSTANCEOF"                { return INSTANCEOF; }
  "CHECKCAST"                 { return CHECKCAST; }
  "TABLESWITCH"               { return TABLESWITCH; }

  {REFERENCE_TYPE}            { return REFERENCE_TYPE; }
  {REFERENCE_TYPE_ARRAY}      { return REFERENCE_TYPE_ARRAY; }
  {DESC_PRIMITIVE}            { return DESC_PRIMITIVE; }
  "["                         { return GROUP_OPENER; }//needs to be after desc because desc will match [ for array
  "]"                         { return GROUP_CLOSER; }
  {METHOD_DESC_TOKEN}         { return METHOD_DESC_TOKEN; }
  {LABEL_ID}                  { return LABEL_ID; }
  {TYPE_NAME}                 { return TYPE_NAME; }
  {IDENTIFIER}                { return IDENTIFIER; }
  {NUMBER}                    { return NUMBER; }
  {STRING}                    { return STRING; }
}

<FRAME_INSN> {
    "SAME"                      { yybegin(YYINITIAL); return SAME; }
    "SAME1"                     { yybegin(YYINITIAL); return SAME1; }
    "CHOP"                      { yybegin(YYINITIAL); return CHOP; }
    "FULL"                      { yybegin(WAITING_GROUP); groupStateWaitingDouble = true; return FULL; }
    "NEW"                       { yybegin(WAITING_GROUP); groupStateWaitingDouble = true; return NEW; }
    "APPEND"                    { yybegin(WAITING_GROUP); groupStateWaitingDouble = false; return APPEND; }
    [^]                         { yybegin(YYINITIAL); groupStateWaitingDouble = false; return BAD_CHARACTER; }
}

<WAITING_GROUP> {
    "["                         { yybegin(INSIDE_GROUP); return GROUP_OPENER; }
    [^]                         { yybegin(YYINITIAL); groupStateWaitingDouble = false; return BAD_CHARACTER; }
}

<INSIDE_GROUP> {
    {REFERENCE_TYPE}            { return REFERENCE_TYPE; }
    {TYPE_NAME}                 { return TYPE_NAME; }
    {REFERENCE_TYPE_ARRAY}      { return REFERENCE_TYPE_ARRAY; }
    {DESC_PRIMITIVE}            { return DESC_PRIMITIVE; }
    "]"                         {
        if (groupStateWaitingDouble){
            yybegin(WAITING_GROUP);
            groupStateWaitingDouble = false;
        } else{
            yybegin(YYINITIAL);
        }
        return GROUP_CLOSER;
    }
    [^]                         { yybegin(YYINITIAL); return BAD_CHARACTER; }
}

[^] { return BAD_CHARACTER; }
