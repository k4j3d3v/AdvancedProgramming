import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class TestAlgs {
	/*
	 * Method for loading KeyRegistry
	 */
	protected static KeyRegistry loadKeyRegistry(String filePath)
	{
		Scanner scanner = null;
	    try {
			scanner = new Scanner(new File(filePath));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    KeyRegistry k = new KeyRegistry();
		String [] splittedLine= null;
		String className, key;
		while(scanner.hasNextLine())
		{
			splittedLine = scanner.nextLine().split(" ");
			className = splittedLine[0];
			key = splittedLine[1];
			Class<?> c = null;
			try {
				c = Class.forName(className) ;
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
			if(c != null )
			{
				k.add(c, key);
			}
			
		}
		return k;
	}
	/*
	 * Method that checks if class c represents an encryption algorithm.
	 * If it is, stores enc/decr methods, for avoiding other future scannings
	 */
	protected static boolean hasConstructor(Class<?> c)
	{
		boolean publConstr;
		try {
			publConstr = c.getConstructor(String.class) != null;
		} catch (NoSuchMethodException e) {
			publConstr = false;
		}
			
		return publConstr;
	}
	protected static boolean hasOneStringParam(Method m)
	{
		return m.getParameterCount()==1 && m.getParameterTypes()[0]== String.class;	
	}
	protected static boolean isEncryptionAlg(Class<?> c, HashMap<Class<?>,HashMap<String,Method>> algEncDecMethods)
	{
		
		boolean encM = false, decM= false;

		Method [] methods = c.getDeclaredMethods();
		Method encMRef = null, decMRef = null;
		for(int i=0; i < methods.length && !(encM && decM); i++ )
		{
			Method m = methods[i];
			if (m.getName().startsWith("enc") && hasOneStringParam(m))
			{	
				encM = true;
				encMRef=m;
			}
			
			if (m.getName().startsWith("dec") && hasOneStringParam(m))
			{
				decM = true;
				decMRef= m;
			}
		}
		if(hasConstructor(c) && encM && decM)
		{
			HashMap<String, Method> mappingMethods = new HashMap<String, Method>();
			// just a mapping for more straightforward access to alg's methods
			mappingMethods.put("enc", encMRef);
			mappingMethods.put("dec", decMRef);

			algEncDecMethods.put(c, mappingMethods);
			
			return true; 
		}
		return false;
	}
	/*
	 * Method testing the algorithm.
	 * (Here exploiting the direct access provided by the previous created data structures.)
	 */
	protected static void testAlg(String secretPath, Class<?> alg, KeyRegistry kr, HashMap<Class<?>,HashMap<String,Method>> algEncDecMethods)
	{
		try {
			Scanner scanner = new Scanner(new File(secretPath));
			while(scanner.hasNextLine())
			{
				String wrd = scanner.nextLine();

				Constructor<?> ctor = alg.getConstructor(String.class);
				Object algInst = ctor.newInstance(kr.get(alg));
				Method encr = algEncDecMethods.get(alg).get("enc"), decr = algEncDecMethods.get(alg).get("dec");
				String encwrd = (String) encr.invoke(algInst, wrd);
				String decwrd = (String) decr.invoke(algInst, encwrd);
				if(!decwrd.equals(wrd) && !(decwrd.startsWith(wrd) && decwrd.matches(wrd+"#*")))
				{
					System.out.println("KO: "+ wrd +" -> " + encwrd +" -> "+ decwrd);
				}
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public static void main(String[] args) {
		
		// set paths
		String cryptoParent = args[0];
		final String keysPath = cryptoParent+"crypto"+File.separator+"keys.list";
		final String secretPath = keysPath.replace("keys", "secret");
		// Loading key registry
		KeyRegistry kr = loadKeyRegistry(keysPath);
		
		// getting the encryption algorithms (the ones holding the keys)
		ArrayList<Class<?>> encrAlgs = kr.getEncrAlgs();
		
		// a simple mapping for storing the algorithms' encryption/decryption methods,
		// filled when checking if it is
		HashMap<Class<?>, HashMap<String,Method>> algEncDecMethods = new HashMap<>();
		for(Class<?> alg : encrAlgs)
		{
			if(!isEncryptionAlg(alg, algEncDecMethods))
				System.out.println("Warning! "+alg.getName()+" is not an encryption algorithm!");
			else
			{
				System.out.println(alg.getName()+" is an Encryption alg");
				testAlg(secretPath, alg, kr,algEncDecMethods);
			}
		}
		
	}

}
