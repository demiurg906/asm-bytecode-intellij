package com.thiakil.idea.textified;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.tree.IElementType;
import com.thiakil.idea.TextifiedAsmLexer;
import com.thiakil.idea.psi.TextifiedAsmTypes;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created by Thiakil on 20/01/2018.
 */
public class TextifiedLexer extends LookAheadLexer {

	private Pattern p1 = Pattern.compile("");

	public TextifiedLexer(){
		super(new TextifiedAsmLexer());
	}

	@Override
	protected void lookAhead(@NotNull Lexer baseLexer) {
		final IElementType tokenType = baseLexer.getTokenType();
		if (tokenType == TextifiedAsmTypes.SINGLE_LINE_COMMENT){
			String comment = baseLexer.getTokenText();

		}
		super.lookAhead(baseLexer);
	}
}
