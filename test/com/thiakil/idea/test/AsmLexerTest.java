package com.thiakil.idea.test;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.thiakil.idea.TextifiedAsmLexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AsmLexerTest {
	public static void main(String[] args) {
		String loaded;
		try (InputStream is = new FileInputStream("C:\\Users\\xander\\git\\PlayNice\\src\\main\\java\\com\\thiakil\\playnice\\test.ow-asm")){
			loaded = new String(FileUtil.loadBytes(is), StandardCharsets.UTF_8);
		} catch (IOException e){
			throw new RuntimeException(e);
		}
		
		Lexer lexer = new TextifiedAsmLexer();
		lexer.start(loaded);
		while (lexer.getTokenType() != null) {
			System.out.printf("%s : %s\n", lexer.getTokenType().toString(), lexer.getTokenText());
			lexer.advance();
		}
	}
}
