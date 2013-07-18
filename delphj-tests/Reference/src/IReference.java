import java.lang.reflect.Method;

interface IReference<T> {

	<X> IReference<X> loadField(String fieldName);

	public <X> X invoke(String name, Object[] args);
	
	public <X> X invoke(String name, Class<?>[] cls, Object[] args);

	T getData();
	
	IReference getNext();
}
