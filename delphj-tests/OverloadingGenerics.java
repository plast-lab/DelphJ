import java.util.Collection;

public class OverloadingGenerics{
	void Test(Collection<Integer> collection) {	}
	void Test(Collection<Float> collection){ }
}