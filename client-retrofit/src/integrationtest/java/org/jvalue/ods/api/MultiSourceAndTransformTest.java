package org.jvalue.ods.api;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.views.generic.TransformationFunctionDescription;
import retrofit.RestAdapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MultiSourceAndTransformTest extends AbstractApiTest {

	private static String transformFunctionAfterSave;
	private static String transformFunctionBeforeSave;

	private ProcessorChainApi processorChainApi;
	private DataTransformationApi transformationApi;

	private final String MULTI_FILTER = "multiFilter";
	private TransformationFunctionDescription queryTransformation;


	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		transformFunctionAfterSave = resourceFileToString("transformAfterStore.js");
		transformFunctionBeforeSave = resourceFileToString("transformBeforeStore.js");
	}

	@Override
	protected void initApi(RestAdapter restAdapter) {
		transformationApi = restAdapter.create(DataTransformationApi.class);
		queryTransformation = new TransformationFunctionDescription(transformFunctionAfterSave);
		processorChainApi = restAdapter.create(ProcessorChainApi.class);
	}


	private LinkedList<ProcessorReference> twoJsonAdapterAndTransformProcessorChain() {
		LinkedList<ProcessorReference> processors = new LinkedList<>();


		// MultiSourceAdapter
		Map<String, String> OpenWeatherMapAdapterOneArgument = new LinkedHashMap<>();
		OpenWeatherMapAdapterOneArgument.put("name", "JsonSourceAdapter");
		OpenWeatherMapAdapterOneArgument.put("sourceUrl", "https://api.openweathermap.org/data/2.5/weather?id=2929567&APPID=a28465af3405af818145d06a431baf88&units=metric&lang=de");


		Map<String, Object> OpenWeatherMapAdapterOne = new LinkedHashMap<>();
		OpenWeatherMapAdapterOne.put("source", OpenWeatherMapAdapterOneArgument);
		OpenWeatherMapAdapterOne.put("alias", "weatherErlangen");


		Map<String, String> OpenWeatherMapAdapterTwoArgument = new LinkedHashMap<>();
		OpenWeatherMapAdapterTwoArgument.put("name", "JsonSourceAdapter");
		OpenWeatherMapAdapterTwoArgument.put("sourceUrl", "https://api.openweathermap.org/data/2.5/weather?id=2934691&APPID=a28465af3405af818145d06a431baf88&units=metric&lang=de");


		Map<String, Object> OpenWeatherMapAdapterTwo = new LinkedHashMap<>();
		OpenWeatherMapAdapterTwo.put("source", OpenWeatherMapAdapterTwoArgument);
		OpenWeatherMapAdapterTwo.put("alias", "weatherDuisburg");


		ArrayList<Map<String, Object>> multiSourceAdapterList = new ArrayList<>();
		multiSourceAdapterList.add(OpenWeatherMapAdapterOne);
		multiSourceAdapterList.add(OpenWeatherMapAdapterTwo);

		Map<String, Object> multiSourceAdapterArguments = new LinkedHashMap<>();
		multiSourceAdapterArguments.put("sources", multiSourceAdapterList);

		ProcessorReference multiSourceAdapterReference = new ProcessorReference("MultiSourceAdapter", multiSourceAdapterArguments);


		//Transformationfilter
		Map<String, Object> transformationFilterArguments = new LinkedHashMap<>();
		transformationFilterArguments.put("transformationFunction", transformFunctionBeforeSave);
		ProcessorReference transformationFilterReference = new ProcessorReference("TransformationFilter", transformationFilterArguments);

		//AddTimestampFilter
		ProcessorReference addTimestampFilterReference = new ProcessorReference("AddTimestampFilter", new LinkedHashMap<>());

		//DbInsertionFilter
		Map<String, Object> dbInsertionFilterArguments = new LinkedHashMap<>();
		dbInsertionFilterArguments.put("updateData", true);
		ProcessorReference dbInsertionFilterReference = new ProcessorReference("DbInsertionFilter", dbInsertionFilterArguments);

		processors.add(multiSourceAdapterReference);
		processors.add(transformationFilterReference);
		processors.add(addTimestampFilterReference);
		processors.add(dbInsertionFilterReference);

		return processors;
	}


	@Test
	public void fetchToTransformToStoreToGetViewDataTest() throws InterruptedException, TimeoutException {

		LinkedList<ProcessorReference> processorReferences = twoJsonAdapterAndTransformProcessorChain();

		ExecutionInterval interval = new ExecutionInterval(60, TimeUnit.MINUTES);
		ProcessorReferenceChainDescription processorReferenceChainDescription = new ProcessorReferenceChainDescription(processorReferences, interval);

		//add processor chain
		processorChainApi.addProcessorChainSynchronously(sourceId, MULTI_FILTER, processorReferenceChainDescription);

		//add transformation view
		String transformationViewId = "transformationViewTest";
		transformationApi.addViewSynchronously(sourceId, transformationViewId, queryTransformation);

		//check resulting data object
		ArrayList viewSynchronously = new ArrayList();
		int tries = 0;
		while(viewSynchronously.isEmpty()){
			if(tries == 3){
				throw new TimeoutException("timed out waiting on the processor chain execution.");
			}
			Thread.sleep(3000);
			viewSynchronously = (ArrayList) transformationApi.getViewSynchronously(sourceId, transformationViewId, true, null);
			tries++;
		}

		HashMap firstObject = (HashMap) viewSynchronously.get(0);
		Assert.assertTrue(firstObject.keySet().contains("erlangen"));
		Assert.assertTrue(firstObject.keySet().contains("duisburg"));
		Assert.assertTrue(firstObject.keySet().contains("extension"));
		Assert.assertTrue(firstObject.keySet().contains("id"));

		HashMap secondObject = (HashMap) viewSynchronously.get(1);
		Assert.assertTrue(secondObject.keySet().contains("key"));
		Assert.assertEquals("value", secondObject.get("key"));

	}


	private static String resourceFileToString(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(MultiSourceAndTransformTest.class.getClassLoader()
			.getResource("transformation/" + fileName).toURI());

		return new String(Files.readAllBytes(path));
	}
}
