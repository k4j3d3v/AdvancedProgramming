import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TestAlgs {
	/*
	 * Method for loading KeyRegistry
	 */
	protected static KeyRegistry loadKeyRegistry(String krPath) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(krPath));

	
	    KeyRegistry k = new KeyRegistry();
		String [] splittedLine= null;
		String className, key;
		Class<?> c = null;
		while(scanner.hasNextLine())
		{
			splittedLine = scanner.nextLine().split(" ");
			className = splittedLine[0];
			key = splittedLine[1];
			try {
				c = Class.forName(className) ;
			} catch (ClassNotFoundException e) {
				System.err.println("[ERROR] Unable to locate class in KeyRegistry files: "+e.getMessage());
				continue;
			}
			if(c != null )
			{
				k.add(c, key);
			}
			
		}
		return k;
	}

	/*
	 * Method that checks if the provided class c
	 * has a constructor expecting one String parameter
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
	/*
	 * Method that checks if the provided method m
	 * expect a single String parameter
	 */
	protected static boolean hasOneStringParam(Method m)
	{
		return m.getParameterCount()==1 && m.getParameterTypes()[0]== String.class;	
	}
	/*
	 * Method that checks if the provided class c implements an encryption algorithm.
	 * If does, for efficiency purposes we save the encryption and decryption methods' reference,
	 * that will be tested.
	 */
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
	protected static void testAlg(String secretPath, Class<?> alg, KeyRegistry kr, HashMap<Class<?>,HashMap<String,Method>> algEncDecMethods) throws FileNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
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
		
	}
	public static void main(String[] args) {
		
		// set paths
		String cryptoParent = args[0];
		final String keysPath = cryptoParent+"crypto"+File.separator+"keys.list";
		// Assuming the secret words file has the same keys' file parent directory
		// (as in the supplied crypto dir)
		final String secretPath = keysPath.replace("keys", "secret");
		// Loading key registry
		KeyRegistry kr;
		try {
			kr = loadKeyRegistry(keysPath);
		} catch (FileNotFoundException e) {

			System.err.println("[ERROR]Check the inserted parent path! "+e.getMessage());
			return;
		}
		
		// Getting the encryption algorithms (the ones we hold the keys!).
		// Another possible approach would be look for classes, then use owned keys for the available classes.
		// Here we start directly testing algorithm which we have keys.
		// For this reason was added this convenience method to KeyRegistry class.
		ArrayList<Class<?>> encrAlgs = kr.getEncrAlgs();
		
		// a simple mapping for storing the algorithms' encryption/decryption methods,
		// filled when checking if it does
		HashMap<Class<?>, HashMap<String,Method>> algEncDecMethods = new HashMap<>();
		for(Class<?> alg : encrAlgs)
		{
			if(isEncryptionAlg(alg, algEncDecMethods))
			{
				System.out.println(alg.getName()+" is an Encryption algorithm.");
				try {
					testAlg(secretPath, alg, kr,algEncDecMethods);
				} catch (FileNotFoundException e) {
					// if it's keys' file same dir never here
					System.err.println("[ERROR] Unable to find secret words file."+ e.getMessage());
					return;
				}
				catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
			            System.err.println("[ERROR] Trouble while testing a class. " + e.getMessage());
			            continue;
			    }
			}
			else
			{
				System.out.println("[Warning] "+alg.getName()+" is not an encryption algorithm!");
			}
		}
		
	}

}
