class Grammar {
    public Grammar() {}

    protected void init (final ExtendedGrammar x) {
	x.s = new ForwardReferenceParser<Statement>() {
	    Parser<Statement> parser() { 
		return x.s.parse(); 
	    }
	};
    }
}

class ExtendedGrammar extends Grammar {

    public ExtendedGrammar () {
	super.init(this);
    }

    Parser<Statement> s;
}

class Statement extends Parser<Statement> {}

class Parser<T> { 
    Parser<T> parse() { return null; }
}

class ForwardReferenceParser<T> extends Parser<T> {

    //    X x;

    //    public ForwardReferenceParser (X x) { this.x = x; }
}