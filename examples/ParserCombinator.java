class Grammar<X> {

    public Grammar() {}

    protected void init (final X x) {
	<F>[f] for ( Parser<F> f : X.fields )
	x.f = new ForwardReferenceParser<F>() {
	    Parser<F> parse() { 
		return x.f.parse(); 
	    }
	};
    }
}

class ExtendedGrammar extends Grammar<ExtendedGrammar> {
    public ExtendedGrammar () {
	super.init(this);
    }

    Parser<Statement> stmt;
    Parser<IfStatement> ifStmt;
}

class Parser<T> { 
    T parse() { return someParser; }
}

class ForwardReferenceParser<T/*,X*/> extends Parser<T> {
    /*
    X x;

    public ForwardReferenceParser (X x) { this.x = x; }
    */
}