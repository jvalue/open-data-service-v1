package org.jvalue.ods.api;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.views.generic.TransformationFunctionDescription;
import retrofit.RestAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MultiSourceAndTransformTest extends AbstractApiTest {

	protected static final String QUERY_FUNCTION =
		"function transform(doc) {" +
			"if (doc != null) { " +
				"var newdoc = {}; " +
				"newdoc.erlangen = {}; " +
				"newdoc.duisburg = {}; " +
				"newdoc.erlangen.weather = doc.weatherErlangen; " +
				"newdoc.duisburg.weather = doc.weatherDuisburg; " +
				"newdoc.extension = doc.extension; " +
				"newdoc.id = doc.id" +
				"emit(new Date(doc.created_at).getTime(), newdoc)" +
			"} " +
		"};";


	protected static final String EXTENSION_FUNCTION =
		"function transform(doc) {" +
			"	if(doc != null) {" +
			"		doc.someId = \"uniqueID\"" +
			"		doc.extension = \"This is an extension\";" +
			"	}" +
			"	return doc;" +
			"}";

	private ProcessorChainApi processorChainApi;
	private DataTransformationApi transformationApi;

	private final String MULTI_FILTER = "multiFilter";

	private static LinkedList<ProcessorReference> processors;
	private ProcessorReferenceChainDescription processorReferenceChainDescription;
	private TransformationFunctionDescription queryTransformation;
	private String transformationViewId = "transformationViewTest";


	@BeforeClass
	public static void initProcessorChain() {
		processors = new LinkedList<>();


		// MultiSourceAdapter
		Map<String, String> APIXUAdapterArgument = new LinkedHashMap<>();
		APIXUAdapterArgument.put("name", "JsonSourceAdapter");
		APIXUAdapterArgument.put("sourceUrl", "https://api.openweathermap.org/data/2.5/weather?id=2929567&APPID=a28465af3405af818145d06a431baf88&units=metric&lang=de");


		Map<String, Object> APIXUAdapter = new LinkedHashMap<>();
		APIXUAdapter.put("source", APIXUAdapterArgument);
		APIXUAdapter.put("alias", "weatherErlangen");


		Map<String, String> OWMAdapterArgument = new LinkedHashMap<>();
		OWMAdapterArgument.put("name", "JsonSourceAdapter");
		OWMAdapterArgument.put("sourceUrl", "https://api.openweathermap.org/data/2.5/weather?id=2934691&APPID=a28465af3405af818145d06a431baf88&units=metric&lang=de");


		Map<String, Object> OWMAdapter = new LinkedHashMap<>();
		OWMAdapter.put("source", OWMAdapterArgument);
		OWMAdapter.put("alias", "weatherDuisburg");


		ArrayList<Map<String, Object>> multiSourceAdapterList = new ArrayList<>();
		multiSourceAdapterList.add(APIXUAdapter);
		multiSourceAdapterList.add(OWMAdapter);

		Map<String, Object> multiSourceAdapterArguments = new LinkedHashMap<>();
		multiSourceAdapterArguments.put("sources", multiSourceAdapterList);

		ProcessorReference multiSourceAdapterReference = new ProcessorReference("MultiSourceAdapter", multiSourceAdapterArguments);


		//Transformationfilter
		Map<String, Object> transformationFilterArguments = new LinkedHashMap<>();
		transformationFilterArguments.put("transformationFunction", EXTENSION_FUNCTION);
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
	}


	@Override
	protected void initApi(RestAdapter restAdapter) {
		transformationApi = restAdapter.create(DataTransformationApi.class);
		queryTransformation = new TransformationFunctionDescription(QUERY_FUNCTION);
		processorChainApi = restAdapter.create(ProcessorChainApi.class);

		ExecutionInterval interval = new ExecutionInterval(60, TimeUnit.MINUTES);
		processorReferenceChainDescription = new ProcessorReferenceChainDescription(processors, interval);
	}


	@Test
	public void fetchToTransformToSaveToRead() {
		//add processor chain
		processorChainApi.addProcessorChainSynchronously(sourceId, MULTI_FILTER, processorReferenceChainDescription);

		//add transformation view
		transformationApi.addViewSynchronously(sourceId, transformationViewId, queryTransformation);

		//check resulting data object
		Object viewSynchronously = transformationApi.getViewSynchronously(sourceId, transformationViewId, true, null);

	}
}
