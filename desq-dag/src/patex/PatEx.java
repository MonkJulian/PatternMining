package patex;

import fst.BasicFst;
import fst.FstOperations;
import fst.XFst;
import fst.Fst;
import hierarchy.SimpleHierarchy;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import patex.PatExParser.CaptureContext;
import patex.PatExParser.ConcatContext;
import patex.PatExParser.ConcatExpContext;
import patex.PatExParser.ItemContext;
import patex.PatExParser.ItemExpContext;
import patex.PatExParser.OptionalContext;
import patex.PatExParser.ParensContext;
import patex.PatExParser.PlusContext;
import patex.PatExParser.RepeatExpContext;
import patex.PatExParser.RepeatMaxContext;
import patex.PatExParser.RepeatMinContext;
import patex.PatExParser.RepeatMinMaxContext;
import patex.PatExParser.SimpleExpContext;
import patex.PatExParser.StarContext;
import patex.PatExParser.UnionContext;
import patex.PatExParser.UnionExpContext;
import patex.PatExParser.WildCardContext;

/**
 * PatEx.java
 * @author Kaustubh Beedkar {kbeedkar@uni-mannheim.de}
 */
public class PatEx {

	String expression;

	public PatEx(String ex) {
		this.expression = ex;
	}

	public Fst translateToFst() {
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

		return fst;
	}

	public class Visitor extends PatExBaseVisitor<Fst> {
		
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
			int min = Integer.parseInt(ctx.INT(0).getText());
			int max = Integer.parseInt(ctx.INT(1).getText());
			return FstOperations.repeatMinMax(visit(ctx.repeatexp()), min, max);
		}

		@Override
		public Fst visitRepeatMin(RepeatMinContext ctx) {
			int min = Integer.parseInt(ctx.INT().getText());
			return FstOperations.repeatMin(visit(ctx.repeatexp()), min);
		}

		@Override
		public Fst visitRepeatMax(RepeatMaxContext ctx) {
			int max = Integer.parseInt(ctx.INT().getText());
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
			
			int label = Integer.parseInt(ctx.INT().getText());
			
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
}
