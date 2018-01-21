package com.thiakil.idea;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.thiakil.idea.psi.TextifiedAsmTypes.*;

%%

%{
  public _SpecialCommentsLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SpecialCommentsLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%xstates COMPILED_FROM_STATE, SIGNATURE_STATE, DECLARATION_STATE

WHITE_SPACE=\s+

CLASS_VERSION=("//"\s+class\s+version\s+)
ACCESS_FLAGS=("//"\s+access\s+flags\s+)
COMPILED_FROM=("//"\s+compiled\s+from:\s+)
SIGNATURE=("//"\s+signature\s+)
DECLARATION=("//"\s+declaration:\s+)
HANDLE_KIND=("//"\s+handle kind\s+)
NUMBER=(-?[0-9]+(\.[0-9]*)?)
HEX_NUMBER=(0x[0-9a-fA-F]+)

%%

<YYINITIAL,COMPILED_FROM_STATE,SIGNATURE_STATE,DECLARATION_STATE>{
    {WHITE_SPACE}        { return WHITE_SPACE; }
}

<YYINITIAL> {
    {CLASS_VERSION}      { return CLASS_VERSION; }
    {ACCESS_FLAGS}       { return ACCESS_FLAGS; }
    {COMPILED_FROM}      { yybegin(COMPILED_FROM_STATE); return COMPILED_FROM; }
    {SIGNATURE}          { yybegin(SIGNATURE_STATE); return SIGNATURE; }
    {DECLARATION}        { yybegin(DECLARATION_STATE); return DECLARATION; }
    {NUMBER}             { return NUMBER; }
    {HEX_NUMBER}         { return NUMBER; }
}

<COMPILED_FROM_STATE> {
    [^]+                { return COMPILE_SOURCE; }
}
<SIGNATURE_STATE> {
    [^]+                 { return SIGNATURE_VALUE; }
}
<DECLARATION_STATE> {
    [^]+                 { return DECLARATION_VALUE; }
}


[^] { return SINGLE_LINE_COMMENT; }