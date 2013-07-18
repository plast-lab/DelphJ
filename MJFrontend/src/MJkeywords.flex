<YYINITIAL> { 
  ".methods" { return sym(Terminals.METHODS); }  
  ".fields"  { return sym(Terminals.FIELDS);  }
  "no"       { return sym(Terminals.NO);      }
  "some"     { return sym(Terminals.SOME);    }
  "errorif"  { return sym(Terminals.ERRORIF); }
  "error"    { return sym(Terminals.ERROR);   }
}