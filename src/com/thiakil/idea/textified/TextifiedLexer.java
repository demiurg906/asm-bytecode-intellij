package com.thiakil.idea.textified;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.SpecialCommentsLexer;
import com.thiakil.idea.TextifiedAsmLexer;
import com.thiakil.idea.psi.TextifiedAsmTypes;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Thiakil on 20/01/2018.
 */
public class TextifiedLexer extends LookAheadLexer {

	private SpecialCommentsLexer specialCommentsLexer = new SpecialCommentsLexer();

	public TextifiedLexer(){
		super(new TextifiedAsmLexer());
	}

	@Override
	protected void lookAhead(@NotNull Lexer baseLexer) {
		final IElementType tokenType = baseLexer.getTokenType();
		if (tokenType == TextifiedAsmTypes.SINGLE_LINE_COMMENT){
			int offset = baseLexer.getTokenStart();
			List<LexerToken> subTokens = getCommentMatches(baseLexer.getTokenSequence());
			if (subTokens.get(0).tokenType != TextifiedAsmTypes.SINGLE_LINE_COMMENT) {
				for (LexerToken token : subTokens) {
					addToken(offset + token.endPos, token.tokenType);
				}
				baseLexer.advance();
				return;
			}
		}
		super.lookAhead(baseLexer);
	}
	
	/**
	 * Run the comment lexer on the comment, returning a list of tokens with collapsed comment fallbacks.
	 * Does so because otherwise the longest match policy means the SINGLE_LINE_COMMENT token gobbles all.
	 * @param input the token from the parent lexer
	 * @return the list of collapsed tokens
	 */
	public List<LexerToken> getCommentMatches(CharSequence input){
		LinkedList<LexerToken> destList = new LinkedList<>();
		specialCommentsLexer.start(input);
		while (specialCommentsLexer.getTokenType() != null) {
			//System.out.printf("%s : %s\n", lexer.getTokenType().toString(), lexer.getTokenText());
			LexerToken last = destList.peekLast();
			if (last != null && (last.tokenType == specialCommentsLexer.getTokenType() || (specialCommentsLexer.getTokenType() == TokenType.WHITE_SPACE && last.tokenType == TextifiedAsmTypes.SINGLE_LINE_COMMENT))){
				last.endPos += (specialCommentsLexer.getTokenEnd()-specialCommentsLexer.getTokenStart());
			} else {
				LexerToken token = new LexerToken();
				token.startPos = specialCommentsLexer.getTokenStart();
				token.endPos = specialCommentsLexer.getTokenEnd();
				token.tokenType = specialCommentsLexer.getTokenType();
				destList.addLast(token);
			}
			specialCommentsLexer.advance();
		}
		return destList;
	}
	
	private static class LexerToken {
		public int startPos;
		public int endPos;
		public IElementType tokenType;
	}
}
