import java.lang.instrument.Instrumentation;

//Class with premain method 
public class ImmunioMain {
	//premain method
	public static void premain(String agentArguments, Instrumentation instrumentation) {	
    		instrumentation.addTransformer(new ImmunioTransformer());
  }	
}

