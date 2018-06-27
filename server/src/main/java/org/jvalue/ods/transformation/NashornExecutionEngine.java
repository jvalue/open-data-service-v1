package org.jvalue.ods.transformation;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NashornExecutionEngine extends AbstractExecutionEngine
{
	private NashornSandbox nashornSandbox;

	//script language specific function call at the beginning of the script
	private static final String CALL_TRANSFORMATION_FUNCTION = "transform("+ GENERIC_DATA_STRING +");";
	
	private void initNashornSandbox(){
		//configure the nashorn sandbox
		nashornSandbox = NashornSandboxes.create();
		nashornSandbox.setMaxCPUTime(5000);
		nashornSandbox.allowNoBraces(true);
		nashornSandbox.setExecutor(Executors.newSingleThreadExecutor());
	}

	@Override
	public String execute(Object data, TransformationFunction transformationFunction)
	throws ScriptException, IOException
	{
		initNashornSandbox();
		try
		{

			//append custom transformation function to wrapper script
			String script = CALL_TRANSFORMATION_FUNCTION + transformationFunction.getTransformFunction();

			//execute script
			nashornSandbox.inject(GENERIC_DATA_STRING, data.toString());
			String result = (String) nashornSandbox.eval(script);
			return result;
		}
		finally
		{
			ExecutorService executor = nashornSandbox.getExecutor();
			executor.shutdown();
		}
	}
}
