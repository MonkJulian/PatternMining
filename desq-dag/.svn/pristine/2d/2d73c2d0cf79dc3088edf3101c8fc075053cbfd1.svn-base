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
	repeatexp ' ' concatexp 		#concat
	| repeatexp						#repeatExp
;
repeatexp
:
	repeatexp '?'					#optional
	|repeatexp '*'					#star
	|repeatexp '+'					#plus
	|repeatexp '{' INT '}'          #repeatMax
	|repeatexp '{' INT ',' '}'      #repeatMin
	|repeatexp '{' INT ',' INT '}'  #repeatMinMax
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
	ANY '^'?                        #wildCard
	| INT '='? '^'?                 #item
;

ANY : '.' ;
INT : [0-9]+ ;
WS  : [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines
