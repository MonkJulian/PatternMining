// Generated from PatEx.g4 by ANTLR 4.5
package parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PatExParser}.
 */
public interface PatExListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code unionExp}
	 * labeled alternative in {@link PatExParser#patex}.
	 * @param ctx the parse tree
	 */
	void enterUnionExp(PatExParser.UnionExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionExp}
	 * labeled alternative in {@link PatExParser#patex}.
	 * @param ctx the parse tree
	 */
	void exitUnionExp(PatExParser.UnionExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code union}
	 * labeled alternative in {@link PatExParser#unionexp}.
	 * @param ctx the parse tree
	 */
	void enterUnion(PatExParser.UnionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code union}
	 * labeled alternative in {@link PatExParser#unionexp}.
	 * @param ctx the parse tree
	 */
	void exitUnion(PatExParser.UnionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code concatExp}
	 * labeled alternative in {@link PatExParser#unionexp}.
	 * @param ctx the parse tree
	 */
	void enterConcatExp(PatExParser.ConcatExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code concatExp}
	 * labeled alternative in {@link PatExParser#unionexp}.
	 * @param ctx the parse tree
	 */
	void exitConcatExp(PatExParser.ConcatExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code concat}
	 * labeled alternative in {@link PatExParser#concatexp}.
	 * @param ctx the parse tree
	 */
	void enterConcat(PatExParser.ConcatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code concat}
	 * labeled alternative in {@link PatExParser#concatexp}.
	 * @param ctx the parse tree
	 */
	void exitConcat(PatExParser.ConcatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code repeatExp}
	 * labeled alternative in {@link PatExParser#concatexp}.
	 * @param ctx the parse tree
	 */
	void enterRepeatExp(PatExParser.RepeatExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code repeatExp}
	 * labeled alternative in {@link PatExParser#concatexp}.
	 * @param ctx the parse tree
	 */
	void exitRepeatExp(PatExParser.RepeatExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code repeatMinMax}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void enterRepeatMinMax(PatExParser.RepeatMinMaxContext ctx);
	/**
	 * Exit a parse tree produced by the {@code repeatMinMax}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void exitRepeatMinMax(PatExParser.RepeatMinMaxContext ctx);
	/**
	 * Enter a parse tree produced by the {@code star}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void enterStar(PatExParser.StarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code star}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void exitStar(PatExParser.StarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code repeatMin}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void enterRepeatMin(PatExParser.RepeatMinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code repeatMin}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void exitRepeatMin(PatExParser.RepeatMinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code repeatMax}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void enterRepeatMax(PatExParser.RepeatMaxContext ctx);
	/**
	 * Exit a parse tree produced by the {@code repeatMax}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void exitRepeatMax(PatExParser.RepeatMaxContext ctx);
	/**
	 * Enter a parse tree produced by the {@code optional}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void enterOptional(PatExParser.OptionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code optional}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void exitOptional(PatExParser.OptionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleExp}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExp(PatExParser.SimpleExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleExp}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExp(PatExParser.SimpleExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plus}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void enterPlus(PatExParser.PlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plus}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 */
	void exitPlus(PatExParser.PlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code itemExp}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 */
	void enterItemExp(PatExParser.ItemExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code itemExp}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 */
	void exitItemExp(PatExParser.ItemExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parens}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 */
	void enterParens(PatExParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 */
	void exitParens(PatExParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code capture}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 */
	void enterCapture(PatExParser.CaptureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code capture}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 */
	void exitCapture(PatExParser.CaptureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wildCard}
	 * labeled alternative in {@link PatExParser#itemexp}.
	 * @param ctx the parse tree
	 */
	void enterWildCard(PatExParser.WildCardContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wildCard}
	 * labeled alternative in {@link PatExParser#itemexp}.
	 * @param ctx the parse tree
	 */
	void exitWildCard(PatExParser.WildCardContext ctx);
	/**
	 * Enter a parse tree produced by the {@code item}
	 * labeled alternative in {@link PatExParser#itemexp}.
	 * @param ctx the parse tree
	 */
	void enterItem(PatExParser.ItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code item}
	 * labeled alternative in {@link PatExParser#itemexp}.
	 * @param ctx the parse tree
	 */
	void exitItem(PatExParser.ItemContext ctx);
}