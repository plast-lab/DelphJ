package examples;

import java.util.*;

public class SynchronizeMe<interface T> implements T {
    final T me;
    final Object mutex;

    public SynchronizeMe (T t) {
	me = t;
	mutex = this;
    }

    <R1,A1*,E1*>[m1] for ( !final R1 m1 (A1) throws E1 : T.methods) 
    public R1 m1 (A1 args) throws E1 {
	return me.m1(args);
    }

    <A2*,E2*>[m2] for ( !final void m2 (A2) throws E2 : T.methods )
    public void m2 (A2 args) throws E2 {
	me.m2(args);
    }
}
