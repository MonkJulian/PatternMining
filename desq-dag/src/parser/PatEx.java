package parser;


import fst.BasicFst;
import fst.FstOperations;
import fst.Fst;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.PatExParser.CaptureContext;
import parser.PatExParser.ConcatContext;
import parser.PatExParser.ConcatExpContext;
import parser.PatExParser.ItemContext;
import parser.PatExParser.ItemExpContext;
import parser.PatExParser.OptionalContext;
import parser.PatExParser.ParensContext;
import parser.PatExParser.PlusContext;
import parser.PatExParser.RepeatExpContext;
import parser.PatExParser.RepeatMaxContext;
import parser.PatExParser.RepeatMinContext;
import parser.PatExParser.RepeatMinMaxContext;
import parser.PatExParser.SimpleExpContext;
import parser.PatExParser.StarContext;
import parser.PatExParser.UnionContext;
import parser.PatExParser.UnionExpContext;
import parser.PatExParser.WildCardContext;
import utils.Dictionary;


/**
 * PatEx.java
 * @author Kaustubh Beedkar {kbeedkar@uni-mannheim.de}
 */
public class PatEx {

	/**
	 * Translates a pattern expression to FST
	 * @param expression
	 * @return minimal Fst
	 */
	public static Fst translateToFst(String expression) {
		return translateToFst(expression, true);
	}
	
	public static Fst translateToFst(String expression, boolean minimize) {
		ANTLRInputStream input = new ANTLRInputStream(expression);

		// Lexer
		PatExLexer lexer = new PatExLexer(input);

		// Tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Parser
		PatExParser parser = new PatExParser(tokens);

		// Parse tree
		ParseTree tree = parser.patex();

		// Visitor for parse tree
		Visitor visitor = new Visitor();

		// Create FST from the syntax tree
		Fst fst = visitor.visit(tree);

		if(minimize) {
			fst.minimize();
		}
		return fst;
	}
	
	public static class Visitor extends PatExBaseVisitor<Fst> {
		private boolean capture = false;

		@Override
		public Fst visitUnionExp(UnionExpContext ctx) {
			return visit(ctx.unionexp());
		}

		@Override
		public Fst visitCapture(CaptureContext ctx) {
			capture = true;
			Fst nfa = visit(ctx.unionexp());
			capture = false;
			return nfa;
		}

		@Override
		public Fst visitUnion(UnionContext ctx) {
			return FstOperations.union(visit(ctx.concatexp()), visit(ctx.unionexp()));
		}

		@Override
		public Fst visitConcatExp(ConcatExpContext ctx) {
			return visit(ctx.getChild(0));
		}

		@Override
		public Fst visitConcat(ConcatContext ctx) {
			return FstOperations.concatenate(visit(ctx.repeatexp()), visit(ctx.concatexp()));
		}

		@Override
		public Fst visitRepeatExp(RepeatExpContext ctx) {
			return visit(ctx.repeatexp());
		}

		@Override
		public Fst visitSimpleExp(SimpleExpContext ctx) {
			return visit(ctx.simpleexp());
		}

		@Override
		public Fst visitStar(StarContext ctx) {
			return FstOperations.kleene(visit(ctx.repeatexp()));
		}

		@Override
		public Fst visitOptional(OptionalContext ctx) {
			return FstOperations.optional(visit(ctx.repeatexp()));
		}

		@Override
		public Fst visitPlus(PlusContext ctx) {
			return FstOperations.plus(visit(ctx.repeatexp()));
		}
		
		
		
		@Override
		public Fst visitRepeatMinMax(RepeatMinMaxContext ctx) {
			int min = Integer.parseInt(ctx.WORD(0).getText());
			int max = Integer.parseInt(ctx.WORD(1).getText());
			return FstOperations.repeatMinMax(visit(ctx.repeatexp()), min, max);
		}

		@Override
		public Fst visitRepeatMin(RepeatMinContext ctx) {
			int min = Integer.parseInt(ctx.WORD().getText());
			return FstOperations.repeatMin(visit(ctx.repeatexp()), min);
		}

		@Override
		public Fst visitRepeatMax(RepeatMaxContext ctx) {
			int max = Integer.parseInt(ctx.WORD().getText());
			return FstOperations.repeatMax(visit(ctx.repeatexp()), max);
		}

		@Override
		public Fst visitItemExp(ItemExpContext ctx) {
			return visit(ctx.itemexp());
		}

		@Override
		public Fst visitParens(ParensContext ctx) {
			return visit(ctx.unionexp());
		}

		@Override
		public Fst visitWildCard(WildCardContext ctx) {
			boolean generalize = false;
			if (ctx.getChildCount() > 1) {
				// operator generalize
				generalize = true;
			}
			return BasicFst.translateWildCard(generalize, capture);
		}

		@Override
		public Fst visitItem(ItemContext ctx) {
			boolean generalize = false;
			boolean force = false;
			
			String word = ctx.WORD().getText();
			
			int label = Dictionary.getInstance().getItemId(word);
			
			int opCount = ctx.getChildCount();
			if (opCount == 2) {
				if (ctx.getChild(1).getText().equals("=")) {
					force = true;
				} else {
					generalize = true;
				}
			} else if (opCount == 3) {
				force = true;
				generalize = true;
			}
			return BasicFst.translateItemExpression(label, force, generalize, capture);
		}
	
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
