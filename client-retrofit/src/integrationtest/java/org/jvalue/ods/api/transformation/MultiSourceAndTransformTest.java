package org.jvalue.ods.api.transformation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvalue.ods.api.DataTransformationApi;
import org.jvalue.ods.api.ProcessorChainApi;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.views.generic.TransformationFunctionDescription;
import retrofit.RestAdapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MultiSourceAndTransformTest extends BaseMultiSourceTransformTest {


	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		transformationView = resourceFileToString("transformationViewTwoJsonOwm.js");
		transformFunctionBeforeStore = resourceFileToString("transformBeforeStore.js");
	}


	@Override
	protected void initApi(RestAdapter restAdapter) {
		transformationApi = restAdapter.create(DataTransformationApi.class);
		queryTransformation = new TransformationFunctionDescription(transformationView);
		processorChainApi = restAdapter.create(ProcessorChainApi.class);
	}


	@Test
	public void twoJsonSourceMultiTest() throws InterruptedException, TimeoutException {
		LinkedList<ProcessorReference> processorReferences = buildTwoJsonAdapterAndTransformProcessorChain();

		ExecutionInterval interval = new ExecutionInterval(60, TimeUnit.MINUTES);
		ProcessorReferenceChainDescription processorReferenceChainDescription = new ProcessorReferenceChainDescription(processorReferences, interval);

		//add processor chain
		processorChainApi.addProcessorChainSynchronously(sourceId, MULTI_FILTER, processorReferenceChainDescription);

		//add transformation view
		transformationApi.addViewSynchronously(sourceId, transformationViewId, queryTransformation);

		//check resulting data object
		ArrayList viewSynchronously = getViewResult(transformationApi, transformationViewId);

		HashMap firstObject = (HashMap) viewSynchronously.get(0);
		Assert.assertTrue(firstObject.keySet().contains("erlangen"));
		Assert.assertTrue(firstObject.keySet().contains("duisburg"));
		Assert.assertTrue(firstObject.keySet().contains("extension"));
		Assert.assertTrue(firstObject.keySet().contains("id"));

		HashMap secondObject = (HashMap) viewSynchronously.get(1);
		Assert.assertTrue(secondObject.keySet().contains("key"));
		Assert.assertEquals("value", secondObject.get("key"));
	}


	private LinkedList<ProcessorReference> buildTwoJsonAdapterAndTransformProcessorChain() {
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
		transformationFilterArguments.put("transformationFunction", transformFunctionBeforeStore);
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
}
