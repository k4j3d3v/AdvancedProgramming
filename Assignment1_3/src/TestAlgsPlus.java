import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import crypto.annot.Decrypt;
import crypto.annot.Encrypt;

public class TestAlgsPlus extends TestAlgs{
	
	protected static boolean hasAnnotation(Method m, Class<? extends Annotation> annotation)
	{
		return (m.getAnnotationsByType(annotation))!= null;	
	}
	
	protected static boolean isEncryptionAlg(Class<?> c, HashMap<Class<?>, HashMap<String, Method>> algEncDecMethods) 
	{
		boolean testAlgsCheck = TestAlgs.isEncryptionAlg(c, algEncDecMethods);
		if(!testAlgsCheck)
		{
			
			List<Method> encM = Arrays.stream(c.getMethods()).filter(m-> hasAnnotation(m, Encrypt.class) && hasOneStringParam(m)).collect(Collectors.toList());
			List<Method> decM = Arrays.stream(c.getMethods()).filter(m-> hasAnnotation(m, Decrypt.class) && hasOneStringParam(m)).collect(Collectors.toList());	
			if(hasConstructor(c) && encM.size()==1 && decM.size()==1)
			{
				// TBD: insert in mapping!!!
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
