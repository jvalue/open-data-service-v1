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

public class MultiSourceApixuOwmTest extends BaseMultiSourceTransformTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		transformationView = resourceFileToString("transformationViewApixuOwm.js");
		transformFunctionBeforeStore = resourceFileToString("transformBeforeStore.js");
	}


	@Override
	protected void initApi(RestAdapter restAdapter) {
		transformationApi = restAdapter.create(DataTransformationApi.class);
		queryTransformation = new TransformationFunctionDescription(transformationView,null);
		processorChainApi = restAdapter.create(ProcessorChainApi.class);
	}


	@Test
	public void apixuOwmMultiSourceTest() throws InterruptedException, TimeoutException {

		LinkedList<ProcessorReference> processorReferences = axiuOwmProcessorChain();

		ExecutionInterval interval = new ExecutionInterval(60, TimeUnit.MINUTES);
		ProcessorReferenceChainDescription processorReferenceChainDescription = new ProcessorReferenceChainDescription(processorReferences, interval);

		//add processor chain
		processorChainApi.addProcessorChainSynchronously(sourceId, MULTI_FILTER, processorReferenceChainDescription);

		//add transformation view
		transformationApi.addViewSynchronously(sourceId, transformationViewId, queryTransformation);

		//check resulting data object
		ArrayList viewSynchronously = getViewResult(transformationApi, transformationViewId);

		HashMap firstObject = (HashMap) viewSynchronously.get(0);
		Assert.assertTrue(firstObject.keySet().contains("someId"));
		Assert.assertEquals("This is an extension", firstObject.get("extension"));

		HashMap data;
		HashMap location;

		ArrayList<HashMap> apixuAdapterData = (ArrayList) ((HashMap) firstObject.get("ApixuAdapter")).get("data");

		data = apixuAdapterData.get(0);
		location = (HashMap) data.get("location");
		Assert.assertEquals("Erlangen", location.get("city"));

		data = apixuAdapterData.get(1);
		location = (HashMap) data.get("location");
		Assert.assertEquals("Berlin", location.get("city"));

		ArrayList<HashMap> owmAdapterData = (ArrayList) ((HashMap) firstObject.get("OpenWeatherMapAdapter")).get("data");

		data = owmAdapterData.get(0);
		location = (HashMap) data.get("location");
		Assert.assertEquals("Erlangen", location.get("city"));

		data = owmAdapterData.get(1);
		location = (HashMap) data.get("location");
		Assert.assertEquals("Berlin", location.get("city"));
	}


	private LinkedList<ProcessorReference> axiuOwmProcessorChain() {
		LinkedList<ProcessorReference> processors = new LinkedList<>();


		// ----------- APIXUSourceAdapter ----------------
		Map<String, Object> APIXUAdapterArgument = new LinkedHashMap<>();
		APIXUAdapterArgument.put("name", "APIXUSourceAdapter");


		Map<String, Object> latlng = new LinkedHashMap<>();
		latlng.put("lat", 49.592410);
		latlng.put("lng", 11.004174);

		Map<String, Object> coordinate = new LinkedHashMap<>();
		coordinate.put("coordinate", latlng);

		Map<String, Object> berlin = new LinkedHashMap<>();
		berlin.put("city", "Berlin");

		ArrayList<Map<String, Object>> locations = new ArrayList<>();
		locations.add(coordinate);
		locations.add(berlin);

		APIXUAdapterArgument.put("locations", locations);
		APIXUAdapterArgument.put("apiKey", "adf173261cde430a918113040182606");


		Map<String, Object> APIXUAdapter = new LinkedHashMap<>();
		APIXUAdapter.put("source", APIXUAdapterArgument);
		APIXUAdapter.put("alias", "weatherApixuSourceAdapter");

		// ----------- OpenWeatherMapSourceAdapter ----------------
		Map<String, Object> OpenWeatherMapAdapterArgument = new LinkedHashMap<>();
		OpenWeatherMapAdapterArgument.put("name", "OpenWeatherMapSourceAdapter");

		Map<String, Object> hamburg = new LinkedHashMap<>();
		hamburg.put("city", "Hamburg");

		ArrayList<Map<String, Object>> locationsOwm = new ArrayList<>();
		locationsOwm.add(coordinate);
		locationsOwm.add(hamburg);

		OpenWeatherMapAdapterArgument.put("locations", locations);
		OpenWeatherMapAdapterArgument.put("apiKey", "a28465af3405af818145d06a431baf88");


		Map<String, Object> OpenWeatherMapAdapter = new LinkedHashMap<>();
		OpenWeatherMapAdapter.put("source", OpenWeatherMapAdapterArgument);
		OpenWeatherMapAdapter.put("alias", "weatherOpenWeatherMap");


		// ----------- MultiSourceAdapter ----------------
		ArrayList<Map<String, Object>> multiSourceAdapterList = new ArrayList<>();
		multiSourceAdapterList.add(APIXUAdapter);
		multiSourceAdapterList.add(OpenWeatherMapAdapter);

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
