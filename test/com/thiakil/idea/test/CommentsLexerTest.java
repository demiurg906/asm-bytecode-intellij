package com.thiakil.idea.test;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.SpecialCommentsLexer;
import com.thiakil.idea.TextifiedAsmLexer;
import com.thiakil.idea.psi.TextifiedAsmTypes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class CommentsLexerTest {
	private static Lexer lexer = new SpecialCommentsLexer();

	public static void main(String[] args) {
		runLexer("// class version 52.0 (52)\n");
		runLexer("// access flags 0x21\n");
		runLexer("// compiled from: TestFile.java\n");
		runLexer("// signature Ljava/lang/Class<*>;\n");
		runLexer("// declaration: java.lang.Class<?>\n");
		runLexer("//unrelated comment\n");
	}

	private static void runLexer(String input){
		for (LexerToken token : getMatches(input)){
			System.out.printf("%s : %s\n", token.tokenType.toString(), input.substring(token.startPos, token.endPos));
		}
	}

	public static List<LexerToken> getMatches(CharSequence input){
		LinkedList<LexerToken> destList = new LinkedList<>();
		lexer.start(input);
		while (lexer.getTokenType() != null) {
			//System.out.printf("%s : %s\n", lexer.getTokenType().toString(), lexer.getTokenText());
			LexerToken last = destList.peekLast();
			if (last != null && (last.tokenType == lexer.getTokenType() || (lexer.getTokenType() == TokenType.WHITE_SPACE && last.tokenType == TextifiedAsmTypes.SINGLE_LINE_COMMENT))){
				last.endPos += (lexer.getTokenEnd()-lexer.getTokenStart());
			} else {
				LexerToken token = new LexerToken();
				token.startPos = lexer.getTokenStart();
				token.endPos = lexer.getTokenEnd();
				token.tokenType = lexer.getTokenType();
				destList.addLast(token);
			}
			lexer.advance();
		}
		return destList;
	}

	private static class LexerToken {
		public int startPos;
		public int endPos;
		public IElementType tokenType;
	}
}
