grammar PatEx;

//Rules

patex
:
	unionexp						#unionExp
;
unionexp
:
	concatexp '|' unionexp			#union
	| concatexp						#concatExp
;
concatexp
:
	repeatexp concatexp 	    	#concat
	| repeatexp						#repeatExp
;
repeatexp
:
	repeatexp '?'					#optional
	|repeatexp '*'					#star
	|repeatexp '+'					#plus
	|repeatexp '{' WORD '}'         #repeatMax
	|repeatexp '{' WORD  ',' '}'      #repeatMin
	|repeatexp '{' WORD  ',' WORD  '}'  #repeatMinMax
	| simpleexp						#simpleExp
;
simpleexp
:
	itemexp							#itemExp
	| '[' unionexp ']'				#parens
	| '(' unionexp ')'  			#capture
;

itemexp 
:
	'.' '^'?                        #wildCard
	| WORD '='? '^'?                 #item
;


WORD :
	CHAR+
;  

CHAR : ~('|' | '?' | '*' | '+' | '{' | '}' | '[' | ']' | '(' | ')' | '^' | '=' | '.'| ' ' | ',' | '\t' | '\r' | '\n') ; 
//ANY : '.' ;
INT : [0-9]+ ;
WS  : [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines
