import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import crypto.annot.Decrypt;
import crypto.annot.Encrypt;

public class TestAlgsPlus extends TestAlgs{
	/*
	 * Method that checks if the provided method m is annotated with annotation.
	 */
	protected static boolean hasAnnotation(Method m, Class<? extends Annotation> annotation)
	{
		return (m.getAnnotationsByType(annotation))!= null;	
	}
	/*
	 * Method testing the algorithm.
 	 * "Overriden" version, here is added the check for annotated method expecting one string parameter.
 	 * Still mantaining support to efficient access to Encryption/Decryption methods adding them to hashmap
	 */
	protected static boolean isEncryptionAlg(Class<?> c, HashMap<Class<?>, HashMap<String, Method>> algEncDecMethods) 
	{
		boolean testAlgsCheck = TestAlgs.isEncryptionAlg(c, algEncDecMethods);
		if(!testAlgsCheck)
		{
			
			List<Method> encM = Arrays.stream(c.getMethods()).filter(m-> hasAnnotation(m, Encrypt.class) && hasOneStringParam(m)).collect(Collectors.toList());
			List<Method> decM = Arrays.stream(c.getMethods()).filter(m-> hasAnnotation(m, Decrypt.class) && hasOneStringParam(m)).collect(Collectors.toList());	
			if(hasConstructor(c) && encM.size()==1 && decM.size()==1)
			{
				HashMap<String, Method> mappingMethods = new HashMap<String, Method>();
				// just a mapping for more straightforward access to alg's methods
				mappingMethods.put("enc", encM.get(0));
				mappingMethods.put("dec", decM.get(0));

				algEncDecMethods.put(c, mappingMethods);
				//System.out.println("OUT: Annotated Algorithm" + c.getName());
				return true;
			}
		}
			
		return testAlgsCheck;
	}
	
	//just a copy and paste of Parent class' main method for testing purpose
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
