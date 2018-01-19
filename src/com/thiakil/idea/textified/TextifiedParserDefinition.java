package com.thiakil.idea.textified;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.thiakil.idea.TextifiedAsmLexer;
import com.thiakil.idea.parser.TextifiedAsmParser;
import com.thiakil.idea.psi.TextifiedAsmFile;
import com.thiakil.idea.psi.TextifiedAsmTypes;
import org.jetbrains.annotations.NotNull;

public class TextifiedParserDefinition implements ParserDefinition {
	public static final IFileElementType FILE = new IFileElementType(TextifiedBytecodeLanguage.INSTANCE);
	public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
	public static final TokenSet COMMENTS = TokenSet.create(TextifiedAsmTypes.COMMENT);
	public static final TokenSet STRINGS = TokenSet.create(TextifiedAsmTypes.STRING);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new TextifiedAsmLexer();
	}
	
	@Override
	public PsiParser createParser(Project project) {
		return new TextifiedAsmParser();
	}
	
	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}
	
	@NotNull
	@Override
	public TokenSet getWhitespaceTokens() {
		return WHITE_SPACES;
	}
	
	@NotNull
	@Override
	public TokenSet getCommentTokens() {
		return COMMENTS;
	}
	
	@NotNull
	@Override
	public TokenSet getStringLiteralElements() {
		return STRINGS;
	}
	
	@NotNull
	@Override
	public PsiElement createElement(ASTNode node) {
		return TextifiedAsmTypes.Factory.createElement(node);
	}
	
	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new TextifiedAsmFile(viewProvider);
	}
	
	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}
}
