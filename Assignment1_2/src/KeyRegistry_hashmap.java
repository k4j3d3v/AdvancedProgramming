import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class KeyRegistry_hashmap {
	private HashMap<Class, Queue<String>> classKeys= new HashMap<>();

	public KeyRegistry_hashmap() {
		super();
	}
	/*
	 * To add a new key for the crypto algorithm
	 * class c
	 */
	public void add(Class c, String key)
	{
		classKeys.putIfAbsent(c, new LinkedList<String>());
		classKeys.get(key).add(key);
	}
	/*
	 * get the last key associated with the class c
	 */
	public String get(Class c)
	{
		return classKeys.get(c).poll();
		
	}
}
