// Generated from PatEx.g4 by ANTLR 4.5
package parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PatExParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PatExVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code unionExp}
	 * labeled alternative in {@link PatExParser#patex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionExp(PatExParser.UnionExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code union}
	 * labeled alternative in {@link PatExParser#unionexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion(PatExParser.UnionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code concatExp}
	 * labeled alternative in {@link PatExParser#unionexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatExp(PatExParser.ConcatExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code concat}
	 * labeled alternative in {@link PatExParser#concatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcat(PatExParser.ConcatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code repeatExp}
	 * labeled alternative in {@link PatExParser#concatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatExp(PatExParser.RepeatExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code repeatMinMax}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatMinMax(PatExParser.RepeatMinMaxContext ctx);
	/**
	 * Visit a parse tree produced by the {@code star}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStar(PatExParser.StarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code repeatMin}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatMin(PatExParser.RepeatMinContext ctx);
	/**
	 * Visit a parse tree produced by the {@code repeatMax}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatMax(PatExParser.RepeatMaxContext ctx);
	/**
	 * Visit a parse tree produced by the {@code optional}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptional(PatExParser.OptionalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleExp}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExp(PatExParser.SimpleExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plus}
	 * labeled alternative in {@link PatExParser#repeatexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlus(PatExParser.PlusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code itemExp}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItemExp(PatExParser.ItemExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(PatExParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code capture}
	 * labeled alternative in {@link PatExParser#simpleexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapture(PatExParser.CaptureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wildCard}
	 * labeled alternative in {@link PatExParser#itemexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildCard(PatExParser.WildCardContext ctx);
	/**
	 * Visit a parse tree produced by the {@code item}
	 * labeled alternative in {@link PatExParser#itemexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItem(PatExParser.ItemContext ctx);
}