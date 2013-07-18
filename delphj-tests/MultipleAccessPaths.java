package delphtests;

public class MultipleAccessPaths {
    public static void main(String [ ] args)
    {
	Subj subj = new Subj(); // object s1
	Wrapper w1 = new Wrapper(subj); // object o1
	Wrapper w2 = new Wrapper(subj); // object o2
	Subj alias = w2.ref;
    }
}
