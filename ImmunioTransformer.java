import java.security.ProtectionDomain;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.ClassFileTransformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtBehavior;
import javassist.CannotCompileException;
import javassist.NotFoundException;

//A custom Transformer, implements ClassFileTransformer 
public class ImmunioTransformer implements ClassFileTransformer {
 	
	//No args Constructor
	public ImmunioTransformer() {
    		//super();
  	}
  
	@Override 
  	public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException,
	NotFoundException, CannotCompileException {
		
		//Create a ClassPool instance
		ClassPool pool = ClassPool.getDefault();
    		CtClass ctClass = null;
			try {
		      		ctClass= pool.makeClass(new java.io.ByteArrayInputStream(bytes));
				//check if class name is GreetingController
				if(ctClass.getName().equals("GreetingController")){
					//Grab all methods in the GreetingController
					//Note CtMethod extends CtBehaviour 
					CtBehavior[] methods = ctClass.getDeclaredBehaviors();
					for (int i = 0; i < methods.length; i++) {
						if (methods[i].isEmpty() == false) {
							//check if method name is greeting
							if (methods[i].getName().equals("greeting")) {
								//inject behaviour to execute before greeting method invocation
								methods[i].insertBefore("System.out.println(\"greeting  method invoked at at: \" + new java.util.Date());");
								//inject behaviour to execute after greeting method invocation
								methods[i].insertAfter("System.out.println(\"greeting method completed at \" + new java.util.Date());");
							}
						}
					}
				}
		      		bytes = ctClass.toBytecode();
		    	}	
		    	catch (Exception e) {
		      		e.printStackTrace();
		    	}
		    	finally {
				//detach the ctClass instance
		      		if (ctClass!= null) {
					ctClass.detach();
		      		}
		    	}	
		return bytes;
  	}
}

