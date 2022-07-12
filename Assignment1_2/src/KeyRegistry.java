import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class KeyRegistry {
	private HashMap<Class, String> classKeys= new HashMap<>();

	public KeyRegistry() {
		super();
	}
	/*
	 * To add a new key for the crypto algorithm
	 * class c
	 */
	public void add(Class c, String key)
	{
		classKeys.putIfAbsent(c, key);
	}
	/*
	 * get the last key associated with the class c
	 */
	public String get(Class c)
	{
		return classKeys.get(c);	
	}
	/*
	 * Just a convenience method for exploit the already loaded KeyRegistry
	 */
	public ArrayList<Class> getEncrAlgs()
	{
		ArrayList<Class> algs = new ArrayList<Class>();
		algs.addAll(classKeys.keySet());
		return algs;
	}
}
